package com.jobtrackr.dto;

import com.jobtrackr.enums.ActivityType;

import java.time.LocalDateTime;

public class ApplicationActivityResponse {

    private Long id;
    private Long applicationId;
    private String companyName;
    private String jobRole;
    private ActivityType activityType;
    private String title;
    private String description;
    private LocalDateTime activityDate;
    private LocalDateTime createdAt;

    public ApplicationActivityResponse(
            Long id,
            Long applicationId,
            String companyName,
            String jobRole,
            ActivityType activityType,
            String title,
            String description,
            LocalDateTime activityDate,
            LocalDateTime createdAt) {

        this.id = id;
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.activityType = activityType;
        this.title = title;
        this.description = description;
        this.activityDate = activityDate;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getApplicationId() { return applicationId; }
    public String getCompanyName() { return companyName; }
    public String getJobRole() { return jobRole; }
    public ActivityType getActivityType() { return activityType; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getActivityDate() { return activityDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}