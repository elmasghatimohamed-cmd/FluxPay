package ma.fluxpay.finance.services.implementation;

import ma.fluxpay.finance.domain.Money;
import ma.fluxpay.finance.domain.entities.FundsReservation;
import ma.fluxpay.finance.domain.enums.ReservationStatus;
import ma.fluxpay.finance.repositories.FundsReservationRepository;
import ma.fluxpay.finance.repositories.WalletRepository;
import ma.fluxpay.finance.services.FundsReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class FundsReservationServiceImpl
    implements FundsReservationService {

    private final FundsReservationRepository reservations;
    private final WalletRepository wallets;

    public FundsReservationServiceImpl(
        FundsReservationRepository reservations,
        WalletRepository wallets
    ) {
        this.reservations = reservations;
        this.wallets = wallets;
    }

    @Override
    @Transactional
    public FundsReservation reserve(
        UUID walletId,
        UUID referenceId,
        BigDecimal amount
    ) {
        BigDecimal value = Money.requirePositive(amount);
        Objects.requireNonNull(referenceId);

        wallets.findByIdForUpdate(walletId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Wallet not found: " + walletId
            ));

        if (reservations.existsByReferenceId(referenceId)) {
            throw new IllegalStateException(
                "Reservation reference already exists"
            );
        }

        if (wallets.reserveFunds(walletId, value) != 1) {
            throw new IllegalStateException(
                "Insufficient available funds or inactive wallet"
            );
        }

        FundsReservation reservation = new FundsReservation(
            wallets.getReferenceById(walletId),
            referenceId,
            value
        );

        return reservations.save(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public FundsReservation getReservation(UUID reservationId) {
        return reservations.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Reservation not found: " + reservationId
            ));
    }

    @Override
    @Transactional
    public void release(UUID reservationId) {
        FundsReservation reservation = reservations
            .findByIdForUpdate(reservationId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Reservation not found: " + reservationId
            ));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException(
                "Only active reservations can be released"
            );
        }

        UUID walletId = reservation.getWallet().getId();

        wallets.findByIdForUpdate(walletId)
            .orElseThrow(() -> new IllegalStateException(
                "Reservation wallet not found"
            ));

        if (wallets.releaseFunds(
            walletId, reservation.getAmount()) != 1) {
            throw new IllegalStateException(
                "Cannot release reserved funds"
            );
        }

        if (reservations.transitionFromActive(
            reservationId, ReservationStatus.RELEASED) != 1) {
            throw new IllegalStateException(
                "Reservation release failed"
            );
        }
    }
}
