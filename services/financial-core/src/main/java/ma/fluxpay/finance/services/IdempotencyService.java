package ma.fluxpay.finance.services;

import ma.fluxpay.finance.domain.entities.IdempotencyRecord;

import java.util.Optional;

public interface IdempotencyService {

    IdempotencyRecord begin(
        String key,
        String operationType,
        String requestHash
    );

    Optional<IdempotencyRecord> find(
        String key,
        String operationType
    );

    void complete(
        String key,
        String operationType,
        int responseCode,
        String responseBody
    );

    void fail(
        String key,
        String operationType,
        int responseCode,
        String responseBody
    );
}
