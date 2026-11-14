package ma.fluxpay.finance.services.implementation;

import ma.fluxpay.finance.domain.entities.LedgerAccount;
import ma.fluxpay.finance.domain.entities.Wallet;
import ma.fluxpay.finance.domain.enums.EntrySide;
import ma.fluxpay.finance.domain.enums.LedgerAccountType;
import ma.fluxpay.finance.repositories.LedgerAccountRepository;
import ma.fluxpay.finance.repositories.WalletRepository;
import ma.fluxpay.finance.services.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final LedgerAccountRepository ledgerAccountRepository;

    public WalletServiceImpl(
        WalletRepository walletRepository,
        LedgerAccountRepository ledgerAccountRepository
    ) {
        this.walletRepository = walletRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
    }

    @Override
    @Transactional
    public Wallet createWallet(UUID ownerId) {
        Objects.requireNonNull(ownerId, "Owner ID is required");

        if (walletRepository.existsByOwnerIdAndCurrency(ownerId, "MAD")) {
            throw new IllegalStateException(
                "A MAD wallet already exists for this owner"
            );
        }

        Wallet wallet = walletRepository.saveAndFlush(
            new Wallet(ownerId)
        );

        LedgerAccount account = new LedgerAccount(
            wallet,
            "WALLET:" + wallet.getId(),
            LedgerAccountType.WALLET_LIABILITY,
            EntrySide.CREDIT
        );

        ledgerAccountRepository.save(account);

        return wallet;
    }

    @Override
    @Transactional(readOnly = true)
    public Wallet getWallet(UUID walletId) {
        return walletRepository.findById(walletId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Wallet not found: " + walletId
            ));
    }

    @Override
    @Transactional(readOnly = true)
    public Wallet getWalletByOwner(UUID ownerId) {
        return walletRepository
            .findByOwnerIdAndCurrency(ownerId, "MAD")
            .orElseThrow(() -> new IllegalArgumentException(
                "Wallet not found for owner: " + ownerId
            ));
    }
}
