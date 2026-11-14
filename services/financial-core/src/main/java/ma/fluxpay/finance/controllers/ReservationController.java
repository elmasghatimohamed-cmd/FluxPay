package ma.fluxpay.finance.controllers;

import jakarta.validation.Valid;
import ma.fluxpay.finance.domain.enums.LedgerTransactionType;
import ma.fluxpay.finance.dto.ledger.LedgerTransactionResponse;
import ma.fluxpay.finance.dto.reservation.*;
import ma.fluxpay.finance.services.FundsReservationService;
import ma.fluxpay.finance.services.LedgerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final FundsReservationService reservationService;
    private final LedgerService ledgerService;

    public ReservationController(
            FundsReservationService reservationService,
            LedgerService ledgerService
    ) {
        this.reservationService = reservationService;
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> reserve(
            @Valid @RequestBody CreateReservationRequest request
    ) {
        var reservation = reservationService.reserve(
                request.walletId(),
                request.referenceId(),
                request.amount()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReservationResponse.from(reservation));
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponse> getReservation(
            @PathVariable UUID reservationId
    ) {
        return ResponseEntity.ok(
                ReservationResponse.from(
                        reservationService.getReservation(reservationId)
                )
        );
    }

    @PostMapping("/{reservationId}/release")
    public ResponseEntity<Void> release(
            @PathVariable UUID reservationId
    ) {
        reservationService.release(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reservationId}/capture")
    public ResponseEntity<LedgerTransactionResponse> capture(
            @PathVariable UUID reservationId,
            @Valid @RequestBody CaptureReservationRequest request
    ) {
        var reservation = reservationService.getReservation(reservationId);

        var transaction = ledgerService.captureReservedTransfer(
                reservationId,
                request.destinationWalletId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                LedgerTransactionResponse.from(
                        transaction,
                        reservation.getReferenceId(),
                        LedgerTransactionType.TRANSFER
                )
        );
    }
}
