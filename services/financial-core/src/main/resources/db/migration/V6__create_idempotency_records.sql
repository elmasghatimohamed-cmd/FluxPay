CREATE TABLE idempotency_records (
    id UUID PRIMARY KEY,

    idempotency_key VARCHAR(255) NOT NULL,

    operation_type VARCHAR(100) NOT NULL,

    request_hash VARCHAR(64) NOT NULL,

    status VARCHAR(20) NOT NULL,

    response_code INTEGER,

    response_body TEXT,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_idempotency_key_operation
        UNIQUE (idempotency_key, operation_type),

    CONSTRAINT chk_idempotency_status
        CHECK (status IN (
                        'PROCESSING',
                        'COMPLETED',
                        'FAILED'
            )),

    CONSTRAINT chk_idempotency_response_code
        CHECK (
            response_code IS NULL
                OR response_code BETWEEN 100 AND 599
            )
);

CREATE INDEX idx_idempotency_records_status
    ON idempotency_records(status);
