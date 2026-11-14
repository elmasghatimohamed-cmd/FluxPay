CREATE TABLE funds_reservations (
    id UUID PRIMARY KEY,

    wallet_id UUID NOT NULL,

    reference_id UUID NOT NULL,

    amount NUMERIC(19, 2) NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reservation_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets(id),

    CONSTRAINT uq_reservation_reference
        UNIQUE (reference_id),

    CONSTRAINT chk_reservation_amount
        CHECK (amount > 0),

    CONSTRAINT chk_reservation_status
        CHECK (status IN (
            'ACTIVE',
            'CONFIRMED',
            'RELEASED'
            ))
);

CREATE INDEX idx_funds_reservations_wallet_status
    ON funds_reservations(wallet_id, status);
