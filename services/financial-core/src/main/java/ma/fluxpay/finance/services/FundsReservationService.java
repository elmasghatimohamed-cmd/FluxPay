package ma.fluxpay.finance.services;

import ma.fluxpay.finance.domain.entities.FundsReservation;

import java.math.BigDecimal;
import java.util.UUID;

public interface FundsReservationService {

    FundsReservation reserve(
        UUID walletId,
        UUID referenceId,
        BigDecimal amount
    );

    FundsReservation getReservation(UUID reservationId);

    void release(UUID reservationId);
}
