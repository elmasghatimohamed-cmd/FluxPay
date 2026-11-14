package ma.fluxpay.finance.domain.entities;

import jakarta.persistence.*;
import ma.fluxpay.finance.domain.enums.EntrySide;
import ma.fluxpay.finance.domain.enums.LedgerAccountType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_accounts")
public class LedgerAccount {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", unique = true)
    private Wallet wallet;

    @Column(name = "account_code", nullable = false, unique = true, length = 100)
    private String accountCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false, length = 20)
    private LedgerAccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "normal_side", nullable = false, length = 6)
    private EntrySide normalSide;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected LedgerAccount() {
        // Required by JPA
    }

    public LedgerAccount(
        Wallet wallet,
        String accountCode,
        LedgerAccountType accountType,
        EntrySide normalSide
    ) {
        this.id = UUID.randomUUID();
        this.wallet = wallet;
        this.accountCode = accountCode;
        this.accountType = accountType;
        this.normalSide = normalSide;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public LedgerAccountType getAccountType() {
        return accountType;
    }

    public EntrySide getNormalSide() {
        return normalSide;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
