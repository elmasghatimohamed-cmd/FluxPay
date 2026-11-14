package ma.fluxpay.finance.dto.reservation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateReservationRequest(
        @NotNull UUID walletId,
        @NotNull UUID referenceId,
        @NotNull
        @DecimalMin("0.01")
        @Digits(integer = 17, fraction = 2)
        BigDecimal amount
) {}
