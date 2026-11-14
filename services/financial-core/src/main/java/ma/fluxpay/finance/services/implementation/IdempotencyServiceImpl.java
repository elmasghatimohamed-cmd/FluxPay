package ma.fluxpay.finance.services.implementation;

import ma.fluxpay.finance.domain.entities.IdempotencyRecord;
import ma.fluxpay.finance.domain.enums.IdempotencyStatus;
import ma.fluxpay.finance.repositories.IdempotencyRecordRepository;
import ma.fluxpay.finance.services.IdempotencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyRecordRepository records;

    public IdempotencyServiceImpl(
        IdempotencyRecordRepository records
    ) {
        this.records = records;
    }

    @Override
    @Transactional
    public IdempotencyRecord begin(
        String key,
        String operationType,
        String requestHash
    ) {
        Optional<IdempotencyRecord> existing =
            records.findByIdempotencyKeyAndOperationType(
                key, operationType
            );

        if (existing.isPresent()) {
            IdempotencyRecord record = existing.get();

            if (!record.getRequestHash().equals(requestHash)) {
                throw new IllegalStateException(
                    "Idempotency key reused with a different request"
                );
            }

            return record;
        }

        return records.saveAndFlush(
            new IdempotencyRecord(key, operationType, requestHash)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdempotencyRecord> find(
        String key,
        String operationType
    ) {
        return records.findByIdempotencyKeyAndOperationType(
            key, operationType
        );
    }

    @Override
    @Transactional
    public void complete(
        String key,
        String operationType,
        int responseCode,
        String responseBody
    ) {
        finish(
            key, operationType,
            IdempotencyStatus.COMPLETED,
            responseCode, responseBody
        );
    }

    @Override
    @Transactional
    public void fail(
        String key,
        String operationType,
        int responseCode,
        String responseBody
    ) {
        finish(
            key, operationType,
            IdempotencyStatus.FAILED,
            responseCode, responseBody
        );
    }

    private void finish(
        String key,
        String operationType,
        IdempotencyStatus status,
        int responseCode,
        String responseBody
    ) {
        if (responseCode < 100 || responseCode > 599) {
            throw new IllegalArgumentException(
                "Invalid HTTP response code"
            );
        }

        Objects.requireNonNull(responseBody);

        int updated = records.finishProcessing(
            key,
            operationType,
            status,
            responseCode,
            responseBody
        );

        if (updated != 1) {
            throw new IllegalStateException(
                "Idempotency operation is missing or already finalized"
            );
        }
    }
}
