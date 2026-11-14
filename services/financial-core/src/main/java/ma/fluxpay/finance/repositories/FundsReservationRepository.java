package ma.fluxpay.finance.repositories;

import jakarta.persistence.LockModeType;
import ma.fluxpay.finance.domain.entities.FundsReservation;
import ma.fluxpay.finance.domain.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FundsReservationRepository
    extends JpaRepository<FundsReservation, UUID> {

    Optional<FundsReservation> findByReferenceId(UUID referenceId);

    boolean existsByReferenceId(UUID referenceId);

    List<FundsReservation> findByWallet_IdAndStatus(
        UUID walletId,
        ReservationStatus status
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM FundsReservation r WHERE r.id = :id")
    Optional<FundsReservation> findByIdForUpdate(
        @Param("id") UUID id
    );


    @Modifying
    @Query("""
    UPDATE FundsReservation r
    SET r.status = :newStatus,
        r.updatedAt = CURRENT_TIMESTAMP
    WHERE r.id = :id
      AND r.status = ma.fluxpay.finance.domain.enums.ReservationStatus.ACTIVE
""")
    int transitionFromActive(
        @Param("id") UUID id,
        @Param("newStatus") ReservationStatus newStatus
    );

}
