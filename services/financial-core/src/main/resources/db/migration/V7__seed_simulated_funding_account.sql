INSERT INTO ledger_accounts (
    id,
    wallet_id,
    account_code,
    account_type,
    normal_side
)
VALUES (
gen_random_uuid(),
NULL,
'SYSTEM:SIMULATED_FUNDING:MAD',
'SIMULATED_FUNDING',
'DEBIT'
    )
ON CONFLICT (account_code) DO NOTHING;
