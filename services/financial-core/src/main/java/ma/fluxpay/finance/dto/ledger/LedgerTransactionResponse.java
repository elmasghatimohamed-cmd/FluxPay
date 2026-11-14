package ma.fluxpay.finance.dto.ledger;

import ma.fluxpay.finance.domain.entities.LedgerTransaction;
import ma.fluxpay.finance.domain.enums.LedgerTransactionType;

import java.util.UUID;

public record LedgerTransactionResponse(
        UUID id,
        UUID referenceId,
        LedgerTransactionType transactionType
) {
    public static LedgerTransactionResponse from(
            LedgerTransaction transaction,
            UUID referenceId,
            LedgerTransactionType type
    ) {
        return new LedgerTransactionResponse(
                transaction.getId(),
                referenceId,
                type
        );
    }
}
