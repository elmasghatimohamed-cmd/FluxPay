package ma.fluxpay.finance.services.implementation;

import ma.fluxpay.finance.domain.Money;
import ma.fluxpay.finance.domain.entities.*;
import ma.fluxpay.finance.domain.enums.*;
import ma.fluxpay.finance.repositories.*;
import ma.fluxpay.finance.services.LedgerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class LedgerServiceImpl implements LedgerService {

    private static final String FUNDING_ACCOUNT_CODE =
        "SYSTEM:SIMULATED_FUNDING:MAD";

    private final WalletRepository wallets;
    private final LedgerAccountRepository accounts;
    private final LedgerTransactionRepository transactions;
    private final LedgerEntryRepository entries;
    private final FundsReservationRepository reservations;

    public LedgerServiceImpl(
        WalletRepository wallets,
        LedgerAccountRepository accounts,
        LedgerTransactionRepository transactions,
        LedgerEntryRepository entries,
        FundsReservationRepository reservations
    ) {
        this.wallets = wallets;
        this.accounts = accounts;
        this.transactions = transactions;
        this.entries = entries;
        this.reservations = reservations;
    }

    @Override
    @Transactional
    public LedgerTransaction fundWallet(
        UUID walletId,
        UUID referenceId,
        BigDecimal amount
    ) {
        BigDecimal value = Money.requirePositive(amount);
        Objects.requireNonNull(referenceId);

        lockWallet(walletId);
        ensureNewReference(referenceId);

        LedgerAccount fundingAccount = accounts
            .findByAccountCode(FUNDING_ACCOUNT_CODE)
            .orElseThrow(() -> new IllegalStateException(
                "Simulated funding account is not initialized"
            ));

        if (fundingAccount.getAccountType()
            != LedgerAccountType.SIMULATED_FUNDING
            || fundingAccount.getNormalSide() != EntrySide.DEBIT) {
            throw new IllegalStateException(
                "Invalid simulated funding account configuration"
            );
        }

        LedgerAccount walletAccount = walletAccount(walletId);

        if (wallets.credit(walletId, value) != 1) {
            throw new IllegalStateException(
                "Wallet cannot receive funding"
            );
        }

        return post(
            referenceId,
            LedgerTransactionType.WALLET_FUNDING,
            fundingAccount,
            walletAccount,
            value
        );
    }

    @Override
    @Transactional
    public LedgerTransaction transfer(
        UUID sourceWalletId,
        UUID destinationWalletId,
        UUID referenceId,
        BigDecimal amount
    ) {
        BigDecimal value = Money.requirePositive(amount);
        Objects.requireNonNull(referenceId);

        requireDistinctWallets(sourceWalletId, destinationWalletId);
        lockWalletPair(sourceWalletId, destinationWalletId);
        ensureNewReference(referenceId);

        LedgerAccount sourceAccount = walletAccount(sourceWalletId);
        LedgerAccount destinationAccount = walletAccount(destinationWalletId);

        if (wallets.debitAvailable(sourceWalletId, value) != 1) {
            throw new IllegalStateException(
                "Insufficient available funds or inactive source wallet"
            );
        }

        if (wallets.credit(destinationWalletId, value) != 1) {
            throw new IllegalStateException(
                "Destination wallet cannot receive funds"
            );
        }

        return post(
            referenceId,
            LedgerTransactionType.TRANSFER,
            sourceAccount,
            destinationAccount,
            value
        );
    }

    @Override
    @Transactional
    public LedgerTransaction captureReservedTransfer(
        UUID reservationId,
        UUID destinationWalletId
    ) {
        Objects.requireNonNull(reservationId);

        // Lock the reservation first to serialize confirmation and release.
        FundsReservation reservation = reservations
            .findByIdForUpdate(reservationId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Reservation not found"
            ));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException(
                "Reservation is not active"
            );
        }

        UUID sourceWalletId = reservation.getWallet().getId();
        UUID referenceId = reservation.getReferenceId();
        BigDecimal amount = reservation.getAmount();

        requireDistinctWallets(sourceWalletId, destinationWalletId);
        lockWalletPair(sourceWalletId, destinationWalletId);
        ensureNewReference(referenceId);

        LedgerAccount sourceAccount = walletAccount(sourceWalletId);
        LedgerAccount destinationAccount = walletAccount(destinationWalletId);

        if (wallets.captureReservedFunds(sourceWalletId, amount) != 1) {
            throw new IllegalStateException(
                "Reserved funds cannot be captured"
            );
        }

        if (wallets.credit(destinationWalletId, amount) != 1) {
            throw new IllegalStateException(
                "Destination wallet cannot receive funds"
            );
        }

        if (reservations.transitionFromActive(
            reservationId, ReservationStatus.CONFIRMED) != 1) {
            throw new IllegalStateException(
                "Reservation confirmation failed"
            );
        }

        return post(
            referenceId,
            LedgerTransactionType.TRANSFER,
            sourceAccount,
            destinationAccount,
            amount
        );
    }

    private LedgerTransaction post(
        UUID referenceId,
        LedgerTransactionType type,
        LedgerAccount debitAccount,
        LedgerAccount creditAccount,
        BigDecimal amount
    ) {
        LedgerTransaction transaction = transactions.save(
            new LedgerTransaction(referenceId, type)
        );

        entries.save(new LedgerEntry(
            transaction, debitAccount, EntrySide.DEBIT, amount
        ));

        entries.save(new LedgerEntry(
            transaction, creditAccount, EntrySide.CREDIT, amount
        ));

        return transaction;
    }

    private LedgerAccount walletAccount(UUID walletId) {
        LedgerAccount account = accounts.findByWallet_Id(walletId)
            .orElseThrow(() -> new IllegalStateException(
                "Ledger account missing for wallet: " + walletId
            ));

        if (account.getAccountType()
            != LedgerAccountType.WALLET_LIABILITY
            || account.getNormalSide() != EntrySide.CREDIT) {
            throw new IllegalStateException(
                "Invalid wallet liability account"
            );
        }

        return account;
    }

    private Wallet lockWallet(UUID walletId) {
        return wallets.findByIdForUpdate(walletId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Wallet not found: " + walletId
            ));
    }

    private void lockWalletPair(UUID first, UUID second) {
        if (first.compareTo(second) < 0) {
            lockWallet(first);
            lockWallet(second);
        } else {
            lockWallet(second);
            lockWallet(first);
        }
    }

    private void requireDistinctWallets(UUID source, UUID destination) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(destination);

        if (source.equals(destination)) {
            throw new IllegalArgumentException(
                "Source and destination wallets must differ"
            );
        }
    }

    private void ensureNewReference(UUID referenceId) {
        if (transactions.existsByReferenceId(referenceId)) {
            throw new IllegalStateException(
                "Ledger reference already processed: " + referenceId
            );
        }
    }
}
