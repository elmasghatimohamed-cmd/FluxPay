package ma.fluxpay.finance.domain.entities;

import jakarta.persistence.*;
import ma.fluxpay.finance.domain.enums.EntrySide;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private LedgerTransaction transaction;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private LedgerAccount account;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_side", nullable = false, length = 6)
    private EntrySide entrySide;

    @Column(
        name = "amount",
        nullable = false,
        precision = 19,
        scale = 2
    )
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected LedgerEntry() {
        // Required by JPA
    }

    public LedgerEntry(
        LedgerTransaction transaction,
        LedgerAccount account,
        EntrySide entrySide,
        BigDecimal amount
    ) {
        Objects.requireNonNull(amount, "Amount is required");

        if (amount.signum() <= 0 || amount.scale() > 2
            || amount.precision() - amount.scale() > 17) {
            throw new IllegalArgumentException(
                "Amount must be positive and fit NUMERIC(19,2)"
            );
        }

        this.id = UUID.randomUUID();
        this.transaction = Objects.requireNonNull(transaction);
        this.account = Objects.requireNonNull(account);
        this.entrySide = Objects.requireNonNull(entrySide);
        this.amount = amount.setScale(2);
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public LedgerTransaction getTransaction() {
        return transaction;
    }

    public LedgerAccount getAccount() {
        return account;
    }

    public EntrySide getEntrySide() {
        return entrySide;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
