package ma.fluxpay.finance.services;

import ma.fluxpay.finance.domain.entities.LedgerTransaction;

import java.math.BigDecimal;
import java.util.UUID;

public interface LedgerService {

    LedgerTransaction fundWallet(
        UUID walletId,
        UUID referenceId,
        BigDecimal amount
    );

    LedgerTransaction transfer(
        UUID sourceWalletId,
        UUID destinationWalletId,
        UUID referenceId,
        BigDecimal amount
    );

    LedgerTransaction captureReservedTransfer(
        UUID reservationId,
        UUID destinationWalletId
    );
}
