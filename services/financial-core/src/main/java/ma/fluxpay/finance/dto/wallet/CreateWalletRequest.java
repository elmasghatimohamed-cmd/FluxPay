package ma.fluxpay.finance.dto.wallet;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateWalletRequest(
    @NotNull UUID ownerId
) {}
