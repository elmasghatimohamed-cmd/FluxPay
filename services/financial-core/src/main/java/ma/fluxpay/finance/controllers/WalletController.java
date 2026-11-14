package ma.fluxpay.finance.controllers;

import jakarta.validation.Valid;
import ma.fluxpay.finance.dto.wallet.CreateWalletRequest;
import ma.fluxpay.finance.dto.wallet.WalletResponse;
import ma.fluxpay.finance.services.WalletService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(
        @Valid @RequestBody CreateWalletRequest request
    ) {
        var wallet = walletService.createWallet(request.ownerId());

        return ResponseEntity
            .created(URI.create("/api/v1/wallets/" + wallet.getId()))
            .body(WalletResponse.from(wallet));
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWallet(
        @PathVariable UUID walletId
    ) {
        var wallet = walletService.getWallet(walletId);

        return ResponseEntity.ok(WalletResponse.from(wallet));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<WalletResponse> getWalletByOwner(
        @PathVariable UUID ownerId
    ) {
        var wallet = walletService.getWalletByOwner(ownerId);

        return ResponseEntity.ok(WalletResponse.from(wallet));
    }
}
