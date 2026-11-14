package ma.fluxpay.finance.domain.repositories;

import ma.fluxpay.finance.domain.entities.LedgerTransaction;
import ma.fluxpay.finance.domain.enums.LedgerTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LedgerTransactionRepository
    extends JpaRepository<LedgerTransaction, UUID> {

    Optional<LedgerTransaction> findByReferenceId(UUID referenceId);

    boolean existsByReferenceId(UUID referenceId);

    List<LedgerTransaction> findByTransactionTypeOrderByCreatedAtDesc(
        LedgerTransactionType transactionType
    );
}
