package ma.fluxpay.finance.domain.entities;

import jakarta.persistence.*;
import ma.fluxpay.finance.domain.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "funds_reservations")
public class FundsReservation {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(name = "reference_id", nullable = false, unique = true)
    private UUID referenceId;

    @Column(
        name = "amount",
        nullable = false,
        precision = 19,
        scale = 2
    )
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status = ReservationStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected FundsReservation() {
        // Required by JPA
    }

    public FundsReservation(
        Wallet wallet,
        UUID referenceId,
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
        this.wallet = Objects.requireNonNull(wallet);
        this.referenceId = Objects.requireNonNull(referenceId);
        this.amount = amount.setScale(2);
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
