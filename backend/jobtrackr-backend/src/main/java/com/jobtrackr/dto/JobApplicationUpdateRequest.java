package com.jobtrackr.dto;

import jakarta.validation.constraints.NotBlank;

public class JobApplicationUpdateRequest {

    @NotBlank(message = "Job role is required")
    private String jobRole;

    @NotBlank(message = "Job location is required")
    private String jobLocation;

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }
}
