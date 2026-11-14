CREATE TABLE ledger_accounts (
    id UUID PRIMARY KEY,

    wallet_id UUID UNIQUE REFERENCES wallets(id),

    account_code VARCHAR(100) NOT NULL UNIQUE,

    account_type VARCHAR(20) NOT NULL,

    normal_side VARCHAR(6) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_ledger_account_type
        CHECK (account_type IN (
            'WALLET_LIABILITY',
            'INTERNAL_CLEARING',
            'SIMULATED_FUNDING'
            )),

    CONSTRAINT chk_ledger_normal_side
        CHECK (normal_side IN ('DEBIT', 'CREDIT')),

    CONSTRAINT chk_wallet_liability_account
        CHECK (
            (account_type = 'WALLET_LIABILITY'
                AND wallet_id IS NOT NULL
                AND normal_side = 'CREDIT')
                OR
            (account_type <> 'WALLET_LIABILITY'
                AND wallet_id IS NULL)
            )
);
