CREATE TABLE ledger_transactions (
id UUID PRIMARY KEY,

reference_id UUID NOT NULL,

transaction_type VARCHAR(30) NOT NULL,

created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT uq_ledger_transaction_reference
    UNIQUE (reference_id),

CONSTRAINT chk_ledger_transaction_type
    CHECK (transaction_type IN (
        'WALLET_FUNDING',
        'TRANSFER',
        'REVERSAL'
        ))
);
