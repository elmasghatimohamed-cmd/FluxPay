package ma.fluxpay.finance.dto.reservation;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CaptureReservationRequest(
        @NotNull UUID destinationWalletId
) {}
