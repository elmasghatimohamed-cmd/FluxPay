package ma.fluxpay.finance.domain.entities;

import jakarta.persistence.*;
import ma.fluxpay.finance.domain.enums.IdempotencyStatus;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
    name = "idempotency_records",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_idempotency_key_operation",
        columnNames = {"idempotency_key", "operation_type"}
    )
)
public class IdempotencyRecord {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "idempotency_key", nullable = false, length = 255)
    private String idempotencyKey;

    @Column(name = "operation_type", nullable = false, length = 100)
    private String operationType;

    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private IdempotencyStatus status = IdempotencyStatus.PROCESSING;

    @Column(name = "response_code")
    private Integer responseCode;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected IdempotencyRecord() {
        // Required by JPA
    }

    public IdempotencyRecord(
        String idempotencyKey,
        String operationType,
        String requestHash
    ) {
        this.id = UUID.randomUUID();
        this.idempotencyKey = requireText(
            idempotencyKey, 255, "idempotencyKey"
        );
        this.operationType = requireText(
            operationType, 100, "operationType"
        );
        this.requestHash = requireText(
            requestHash, 64, "requestHash"
        );

        if (!this.requestHash.matches("[0-9a-fA-F]{64}")) {
            throw new IllegalArgumentException(
                "requestHash must be a SHA-256 hex digest"
            );
        }

        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    private static String requireText(
        String value,
        int maxLength,
        String fieldName
    ) {
        Objects.requireNonNull(value, fieldName + " is required");

        if (value.isBlank() || value.length() > maxLength) {
            throw new IllegalArgumentException(
                fieldName + " must contain 1 to "
                    + maxLength + " characters"
            );
        }

        return value;
    }

    public UUID getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public IdempotencyStatus getStatus() {
        return status;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
