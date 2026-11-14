package ma.fluxpay.finance.dto.reservation;

import ma.fluxpay.finance.domain.entities.FundsReservation;
import ma.fluxpay.finance.domain.enums.ReservationStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservationResponse(
        UUID id,
        UUID walletId,
        UUID referenceId,
        BigDecimal amount,
        ReservationStatus status
) {
    public static ReservationResponse from(
            FundsReservation reservation
    ) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getWallet().getId(),
                reservation.getReferenceId(),
                reservation.getAmount(),
                reservation.getStatus()
        );
    }
}
