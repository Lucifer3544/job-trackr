package com.jobtrackr.controller;

import com.jobtrackr.dto.OfferRequest;
import com.jobtrackr.dto.OfferResponse;
import com.jobtrackr.dto.OfferStatusRequest;
import com.jobtrackr.service.OfferService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    // Create a company offer or revised offer
    @PostMapping("/applications/{applicationId}/offers")
    public ResponseEntity<OfferResponse> createOffer(
            @PathVariable Long applicationId,
            @Valid @RequestBody OfferRequest request) {

        OfferResponse response =
                offerService.createOffer(applicationId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all revisions for a job application
    @GetMapping("/applications/{applicationId}/offers")
    public ResponseEntity<List<OfferResponse>> getOffersByApplication(
            @PathVariable Long applicationId) {

        return ResponseEntity.ok(
                offerService.getOffersByApplication(applicationId)
        );
    }

    // Get a specific offer
    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferResponse> getOfferById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                offerService.getOfferById(id)
        );
    }

    // Update a pending offer
    @PutMapping("/offers/{id}")
    public ResponseEntity<OfferResponse> updateOffer(
            @PathVariable Long id,
            @Valid @RequestBody OfferRequest request) {

        return ResponseEntity.ok(
                offerService.updateOffer(id, request)
        );
    }

    // Accept or reject an offer
    @PatchMapping("/offers/{id}/status")
    public ResponseEntity<OfferResponse> updateOfferStatus(
            @PathVariable Long id,
            @Valid @RequestBody OfferStatusRequest request) {

        return ResponseEntity.ok(
                offerService.updateOfferStatus(id, request.getStatus())
        );
    }

    // Delete an offer
    @DeleteMapping("/offers/{id}")
    public ResponseEntity<Void> deleteOffer(
            @PathVariable Long id) {

        offerService.deleteOffer(id);

        return ResponseEntity.noContent().build();
    }
}