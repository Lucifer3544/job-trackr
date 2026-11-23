package com.jobtrackr.controller;

import com.jobtrackr.dto.InterviewResultRequest;
import com.jobtrackr.dto.InterviewRoundRequest;
import com.jobtrackr.dto.InterviewRoundResponse;

import com.jobtrackr.enums.InterviewStatus;

import com.jobtrackr.service.InterviewRoundService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InterviewRoundController {

    private final InterviewRoundService interviewService;

    public InterviewRoundController(
            InterviewRoundService interviewService) {

        this.interviewService = interviewService;
    }

    // Create interview
    @PostMapping("/applications/{applicationId}/interviews")
    @ResponseStatus(HttpStatus.CREATED)
    public InterviewRoundResponse createInterview(
            @PathVariable Long applicationId,
            @Valid @RequestBody InterviewRoundRequest request) {

        return interviewService.createInterview(applicationId, request);
    }

    // Get all interviews for an application
    @GetMapping("/applications/{applicationId}/interviews")
    public List<InterviewRoundResponse> getInterviewsByApplication(
            @PathVariable Long applicationId) {

        return interviewService.getInterviewsByApplication(applicationId);
    }

    // Get interview by ID
    @GetMapping("/interviews/{id}")
    public InterviewRoundResponse getInterviewById(
            @PathVariable Long id) {

        return interviewService.getInterviewById(id);
    }

    // Update interview details
    @PutMapping("/interviews/{id}")
    public InterviewRoundResponse updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRoundRequest request) {

        return interviewService.updateInterview(id, request);
    }

    // Update interview result
    @PatchMapping("/interviews/{id}/result")
    public InterviewRoundResponse updateResult(
            @PathVariable Long id,
            @Valid @RequestBody InterviewResultRequest request) {

        return interviewService.updateResult(id, request);
    }

    // Update interview status
    @PatchMapping("/interviews/{id}/status")
    public InterviewRoundResponse updateStatus(
            @PathVariable Long id,
            @RequestParam @NotNull InterviewStatus status) {

        return interviewService.updateStatus(id, status);
    }

    // Reschedule interview
    @PatchMapping("/interviews/{id}/reschedule")
    public InterviewRoundResponse rescheduleInterview(
            @PathVariable Long id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime scheduledAt) {

        return interviewService.rescheduleInterview(id, scheduledAt);
    }

    // Delete interview
    @DeleteMapping("/interviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInterview(@PathVariable Long id) {

        interviewService.deleteInterview(id);
    }
}
