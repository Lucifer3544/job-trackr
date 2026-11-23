package com.jobtrackr.dto;

import com.jobtrackr.enums.FollowUpStatus;
import com.jobtrackr.enums.FollowUpType;

import java.time.LocalDate;

public class FollowUpResponse {

    private Long id;
    private Long applicationId;
    private String companyName;
    private String jobRole;
    private Long hrContactId;
    private LocalDate followUpDate;
    private FollowUpType followUpType;
    private String description;
    private FollowUpStatus status;
    private String notes;

    public FollowUpResponse(
            Long id,
            Long applicationId,
            String companyName,
            String jobRole,
            Long hrContactId,
            LocalDate followUpDate,
            FollowUpType followUpType,
            String description,
            FollowUpStatus status,
            String notes) {

        this.id = id;
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.hrContactId = hrContactId;
        this.followUpDate = followUpDate;
        this.followUpType = followUpType;
        this.description = description;
        this.status = status;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public Long getApplicationId() { return applicationId; }
    public String getCompanyName() { return companyName; }
    public String getJobRole() { return jobRole; }
    public Long getHrContactId() { return hrContactId; }
    public LocalDate getFollowUpDate() { return followUpDate; }
    public FollowUpType getFollowUpType() { return followUpType; }
    public String getDescription() { return description; }
    public FollowUpStatus getStatus() { return status; }
    public String getNotes() { return notes; }
}