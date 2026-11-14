package ma.fluxpay.finance.domain.entities;

import jakarta.persistence.*;
import ma.fluxpay.finance.domain.enums.LedgerTransactionType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_transactions")
public class LedgerTransaction {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "reference_id", nullable = false, unique = true)
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 30)
    private LedgerTransactionType transactionType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected LedgerTransaction() {
        // Required by JPA
    }

    public LedgerTransaction(
        UUID referenceId,
        LedgerTransactionType transactionType
    ) {
        this.id = UUID.randomUUID();
        this.referenceId = java.util.Objects.requireNonNull(referenceId);
        this.transactionType = java.util.Objects.requireNonNull(transactionType);
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public LedgerTransactionType getTransactionType() {
        return transactionType;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
