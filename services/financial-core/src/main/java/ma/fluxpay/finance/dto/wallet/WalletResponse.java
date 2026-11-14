package ma.fluxpay.finance.dto.wallet;

import ma.fluxpay.finance.domain.entities.Wallet;
import ma.fluxpay.finance.domain.enums.WalletStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record WalletResponse(
    UUID id,
    UUID ownerId,
    String currency,
    BigDecimal balance,
    BigDecimal reservedAmount,
    BigDecimal availableBalance,
    WalletStatus status,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static WalletResponse from(Wallet wallet) {
        return new WalletResponse(
            wallet.getId(),
            wallet.getOwnerId(),
            wallet.getCurrency(),
            wallet.getBalance(),
            wallet.getReservedAmount(),
            wallet.getAvailableBalance(),
            wallet.getStatus(),
            wallet.getCreatedAt(),
            wallet.getUpdatedAt()
        );
    }
}
