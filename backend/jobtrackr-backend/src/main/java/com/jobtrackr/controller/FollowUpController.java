package com.jobtrackr.controller;

import com.jobtrackr.dto.FollowUpRequest;
import com.jobtrackr.dto.FollowUpResponse;
import com.jobtrackr.dto.FollowUpStatusRequest;
import com.jobtrackr.enums.FollowUpStatus;
import com.jobtrackr.service.FollowUpService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FollowUpController {

    private final FollowUpService followUpService;

    public FollowUpController(FollowUpService followUpService) {
        this.followUpService = followUpService;
    }

    // Create a follow-up for a job application
    @PostMapping("/applications/{applicationId}/follow-ups")
    public ResponseEntity<FollowUpResponse> createFollowUp(
            @PathVariable Long applicationId,
            @Valid @RequestBody FollowUpRequest request) {

        FollowUpResponse response =
                followUpService.createFollowUp(applicationId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all follow-ups for a job application
    @GetMapping("/applications/{applicationId}/follow-ups")
    public ResponseEntity<List<FollowUpResponse>> getFollowUpsByApplication(
            @PathVariable Long applicationId) {

        return ResponseEntity.ok(
                followUpService.getFollowUpsByApplication(applicationId)
        );
    }

    // Get follow-ups by status
    @GetMapping("/follow-ups")
    public ResponseEntity<List<FollowUpResponse>> getFollowUpsByStatus(
            @RequestParam FollowUpStatus status) {

        return ResponseEntity.ok(
                followUpService.getFollowUpsByStatus(status)
        );
    }

    // Get a specific follow-up
    @GetMapping("/follow-ups/{id}")
    public ResponseEntity<FollowUpResponse> getFollowUpById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                followUpService.getFollowUpById(id)
        );
    }

    // Update a pending follow-up
    @PutMapping("/follow-ups/{id}")
    public ResponseEntity<FollowUpResponse> updateFollowUp(
            @PathVariable Long id,
            @Valid @RequestBody FollowUpRequest request) {

        return ResponseEntity.ok(
                followUpService.updateFollowUp(id, request)
        );
    }

    // Complete or cancel a follow-up
    @PatchMapping("/follow-ups/{id}/status")
    public ResponseEntity<FollowUpResponse> updateFollowUpStatus(
            @PathVariable Long id,
            @Valid @RequestBody FollowUpStatusRequest request) {

        return ResponseEntity.ok(
                followUpService.updateFollowUpStatus(id, request.getStatus())
        );
    }

    // Delete a follow-up
    @DeleteMapping("/follow-ups/{id}")
    public ResponseEntity<Void> deleteFollowUp(
            @PathVariable Long id) {

        followUpService.deleteFollowUp(id);

        return ResponseEntity.noContent().build();
    }
}