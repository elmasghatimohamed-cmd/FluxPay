package ma.fluxpay.finance.repositories;

import jakarta.persistence.LockModeType;
import ma.fluxpay.finance.domain.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.*;

import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;

public interface WalletRepository
    extends JpaRepository<Wallet, UUID> {

    Optional<Wallet> findByOwnerIdAndCurrency(
        UUID ownerId,
        String currency
    );

    boolean existsByOwnerIdAndCurrency(
        UUID ownerId,
        String currency
    );


    @Modifying
    @Query("""
    UPDATE Wallet w
    SET w.balance = w.balance + :amount,
        w.version = w.version + 1,
        w.updatedAt = CURRENT_TIMESTAMP
    WHERE w.id = :id
      AND w.status = ma.fluxpay.finance.domain.enums.WalletStatus.ACTIVE
""")
    int credit(
        @Param("id") UUID id,
        @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("""
    UPDATE Wallet w
    SET w.balance = w.balance - :amount,
        w.version = w.version + 1,
        w.updatedAt = CURRENT_TIMESTAMP
    WHERE w.id = :id
      AND w.status = ma.fluxpay.finance.domain.enums.WalletStatus.ACTIVE
      AND w.balance - w.reservedAmount >= :amount
""")
    int debitAvailable(
        @Param("id") UUID id,
        @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("""
    UPDATE Wallet w
    SET w.reservedAmount = w.reservedAmount + :amount,
        w.version = w.version + 1,
        w.updatedAt = CURRENT_TIMESTAMP
    WHERE w.id = :id
      AND w.status = ma.fluxpay.finance.domain.enums.WalletStatus.ACTIVE
      AND w.balance - w.reservedAmount >= :amount
""")
    int reserveFunds(
        @Param("id") UUID id,
        @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("""
    UPDATE Wallet w
    SET w.reservedAmount = w.reservedAmount - :amount,
        w.version = w.version + 1,
        w.updatedAt = CURRENT_TIMESTAMP
    WHERE w.id = :id
      AND w.reservedAmount >= :amount
""")
    int releaseFunds(
        @Param("id") UUID id,
        @Param("amount") BigDecimal amount
    );

    @Modifying
    @Query("""
    UPDATE Wallet w
    SET w.balance = w.balance - :amount,
        w.reservedAmount = w.reservedAmount - :amount,
        w.version = w.version + 1,
        w.updatedAt = CURRENT_TIMESTAMP
    WHERE w.id = :id
      AND w.status = ma.fluxpay.finance.domain.enums.WalletStatus.ACTIVE
      AND w.reservedAmount >= :amount
""")
    int captureReservedFunds(
        @Param("id") UUID id,
        @Param("amount") BigDecimal amount
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id = :id")
    Optional<Wallet> findByIdForUpdate(@Param("id") UUID id);

}
