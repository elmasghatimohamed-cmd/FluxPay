CREATE TABLE wallets (
    id UUID PRIMARY KEY,

    owner_id UUID NOT NULL,

    currency VARCHAR(3) NOT NULL DEFAULT 'MAD',

    balance NUMERIC(19, 2) NOT NULL DEFAULT 0,

    reserved_amount NUMERIC(19, 2) NOT NULL DEFAULT 0,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    version BIGINT NOT NULL DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_wallet_currency
        CHECK (currency = 'MAD'),

    CONSTRAINT chk_wallet_balance
        CHECK (balance >= 0),

    CONSTRAINT chk_wallet_reserved
        CHECK (reserved_amount >= 0),

                         CONSTRAINT chk_wallet_available
                             CHECK (reserved_amount <= balance),

                         CONSTRAINT chk_wallet_status
                             CHECK (status IN ('ACTIVE', 'FROZEN', 'CLOSED')),

                         CONSTRAINT uq_wallet_owner_currency
                             UNIQUE (owner_id, currency)
);
