package ma.fluxpay.finance.domain.repositories;

import ma.fluxpay.finance.domain.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

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
}
