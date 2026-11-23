package com.jobtrackr.controller;

import com.jobtrackr.dto.ApplicationActivityRequest;
import com.jobtrackr.dto.ApplicationActivityResponse;
import com.jobtrackr.enums.ActivityType;
import com.jobtrackr.service.ApplicationActivityService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApplicationActivityController {

    private final ApplicationActivityService activityService;

    public ApplicationActivityController(
            ApplicationActivityService activityService) {
        this.activityService = activityService;
    }

    // Create an activity
    @PostMapping("/applications/{applicationId}/activities")
    public ResponseEntity<ApplicationActivityResponse> createActivity(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationActivityRequest request) {

        ApplicationActivityResponse response =
                activityService.createActivity(applicationId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all activities for an application
    @GetMapping("/applications/{applicationId}/activities")
    public ResponseEntity<List<ApplicationActivityResponse>>
            getActivitiesByApplication(
                    @PathVariable Long applicationId) {

        return ResponseEntity.ok(
                activityService.getActivitiesByApplication(applicationId)
        );
    }

    // Filter activities by type
    @GetMapping("/applications/{applicationId}/activities/filter")
    public ResponseEntity<List<ApplicationActivityResponse>> getActivitiesByType(
            @PathVariable Long applicationId,
            @RequestParam ActivityType activityType) {

        return ResponseEntity.ok(
                activityService.getActivitiesByType(
                        applicationId,
                        activityType
                )
        );
    }

    // Get one activity
    @GetMapping("/activities/{id}")
    public ResponseEntity<ApplicationActivityResponse> getActivityById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                activityService.getActivityById(id)
        );
    }

    // Update an activity
    @PutMapping("/activities/{id}")
    public ResponseEntity<ApplicationActivityResponse> updateActivity(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationActivityRequest request) {

        return ResponseEntity.ok(
                activityService.updateActivity(id, request)
        );
    }

    // Delete an activity
    @DeleteMapping("/activities/{id}")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable Long id) {

        activityService.deleteActivity(id);

        return ResponseEntity.noContent().build();
    }
}