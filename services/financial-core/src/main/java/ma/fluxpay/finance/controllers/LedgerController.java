package ma.fluxpay.finance.controllers;

import jakarta.validation.Valid;
import ma.fluxpay.finance.domain.enums.LedgerTransactionType;
import ma.fluxpay.finance.dto.ledger.FundingRequest;
import ma.fluxpay.finance.dto.ledger.TransferRequest;
import ma.fluxpay.finance.dto.ledger.LedgerTransactionResponse;
import ma.fluxpay.finance.services.LedgerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping("/wallets/{walletId}/funding")
    public ResponseEntity<LedgerTransactionResponse> fundWallet(
            @PathVariable UUID walletId,
            @Valid @RequestBody FundingRequest request
    ) {
        var transaction = ledgerService.fundWallet(
                walletId,
                request.referenceId(),
                request.amount()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                LedgerTransactionResponse.from(
                        transaction,
                        request.referenceId(),
                        LedgerTransactionType.WALLET_FUNDING
                )
        );
    }

    @PostMapping("/transfers")
    public ResponseEntity<LedgerTransactionResponse> transfer(
            @Valid @RequestBody TransferRequest request
    ) {
        var transaction = ledgerService.transfer(
                request.sourceWalletId(),
                request.destinationWalletId(),
                request.referenceId(),
                request.amount()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                LedgerTransactionResponse.from(
                        transaction,
                        request.referenceId(),
                        LedgerTransactionType.TRANSFER
                )
        );
    }
}
