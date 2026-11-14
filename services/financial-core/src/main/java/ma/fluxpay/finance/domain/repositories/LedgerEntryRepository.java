package ma.fluxpay.finance.domain.repositories;

import ma.fluxpay.finance.domain.entities.LedgerEntry;
import ma.fluxpay.finance.domain.enums.EntrySide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerEntryRepository
    extends JpaRepository<LedgerEntry, UUID> {

    List<LedgerEntry> findByTransaction_IdOrderByCreatedAtAsc(
        UUID transactionId
    );

    List<LedgerEntry> findByAccount_IdOrderByCreatedAtAsc(
        UUID accountId
    );

    List<LedgerEntry> findByTransaction_IdAndEntrySide(
        UUID transactionId,
        EntrySide entrySide
    );
}
