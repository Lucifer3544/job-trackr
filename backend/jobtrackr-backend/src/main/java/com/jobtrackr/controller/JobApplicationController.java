package com.jobtrackr.controller;

import com.jobtrackr.dto.JobApplicationRequest;
import com.jobtrackr.dto.JobApplicationResponse;
import com.jobtrackr.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.jobtrackr.dto.ApplicationStatusRequest;
import com.jobtrackr.dto.JobApplicationUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(
            JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplicationResponse createApplication(
            @Valid @RequestBody JobApplicationRequest request) {

        return jobApplicationService.createJobApplication(
                request.getCompanyId(),
                request.getJobRole(),
                request.getJobLocation()
        );
    }

    @GetMapping
    public List<JobApplicationResponse> getAllApplications() {
        return jobApplicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    public JobApplicationResponse getApplicationById(
            @PathVariable Long id) {
        return jobApplicationService.getApplicationById(id);
    }

    @GetMapping("/company/{companyId}")
    public List<JobApplicationResponse> getApplicationsByCompany(
            @PathVariable Long companyId) {
        return jobApplicationService.getApplicationsByCompany(companyId);
    }

    @PatchMapping("/{id}/status")
    public JobApplicationResponse updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusRequest request) {

        return jobApplicationService.updateApplicationStatus(
                id,
                request.getStatus()
        );
    }

    @PutMapping("/{id}")
    public JobApplicationResponse updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationUpdateRequest request) {

        return jobApplicationService.updateApplication(
                id,
                request.getJobRole(),
                request.getJobLocation()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteApplication(@PathVariable Long id) {
        jobApplicationService.deleteApplication(id);
    }
}
