package ma.fluxpay.finance.domain.repositories;

import jakarta.persistence.LockModeType;
import ma.fluxpay.finance.domain.entities.IdempotencyRecord;
import ma.fluxpay.finance.domain.enums.IdempotencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdempotencyRecordRepository
    extends JpaRepository<IdempotencyRecord, UUID> {

    Optional<IdempotencyRecord> findByIdempotencyKeyAndOperationType(
        String idempotencyKey,
        String operationType
    );

    boolean existsByIdempotencyKeyAndOperationType(
        String idempotencyKey,
        String operationType
    );

    List<IdempotencyRecord> findByStatus(
        IdempotencyStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT i
            FROM IdempotencyRecord i
            WHERE i.idempotencyKey = :key
              AND i.operationType = :operationType
            """)
    Optional<IdempotencyRecord> findByKeyAndOperationTypeForUpdate(
        @Param("key") String key,
        @Param("operationType") String operationType
    );
}
