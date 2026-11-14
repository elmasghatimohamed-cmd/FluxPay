package ma.fluxpay.finance.services;

import ma.fluxpay.finance.domain.entities.Wallet;

import java.util.UUID;

public interface WalletService {

    Wallet createWallet(UUID ownerId);

    Wallet getWallet(UUID walletId);

    Wallet getWalletByOwner(UUID ownerId);
}
