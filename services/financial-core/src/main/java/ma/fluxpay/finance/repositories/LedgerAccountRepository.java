package ma.fluxpay.finance.repositories;

import ma.fluxpay.finance.domain.entities.LedgerAccount;
import ma.fluxpay.finance.domain.enums.LedgerAccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LedgerAccountRepository
    extends JpaRepository<LedgerAccount, UUID> {

    Optional<LedgerAccount> findByAccountCode(String accountCode);

    Optional<LedgerAccount> findByWallet_Id(UUID walletId);

    boolean existsByAccountCode(String accountCode);

    boolean existsByWallet_Id(UUID walletId);

    Optional<LedgerAccount> findByAccountTypeAndAccountCode(
        LedgerAccountType accountType,
        String accountCode
    );
}
