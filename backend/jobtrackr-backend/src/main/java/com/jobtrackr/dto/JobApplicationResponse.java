package com.jobtrackr.dto;

import com.jobtrackr.enums.ApplicationStatus;

public class JobApplicationResponse {

    private Long id;
    private Long companyId;
    private String companyName;
    private String jobRole;
    private String jobLocation;
    private ApplicationStatus status;

    public JobApplicationResponse(
            Long id,
            Long companyId,
            String companyName,
            String jobRole,
            String jobLocation,
            ApplicationStatus status) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.jobLocation = jobLocation;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public ApplicationStatus getStatus() {
        return status;
    }
}
