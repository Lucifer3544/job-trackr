package com.jobtrackr.dto;

import com.jobtrackr.enums.InterviewResult;
import com.jobtrackr.enums.InterviewStatus;
import com.jobtrackr.enums.InterviewType;

import java.time.LocalDateTime;

public class InterviewRoundResponse {

    private Long id;
    private Long applicationId;
    private String companyName;
    private String jobRole;
    private Integer roundNumber;
    private String roundName;
    private InterviewType interviewType;
    private LocalDateTime scheduledAt;
    private InterviewStatus status;
    private InterviewResult result;
    private String notes;

    public InterviewRoundResponse(
            Long id,
            Long applicationId,
            String companyName,
            String jobRole,
            Integer roundNumber,
            String roundName,
            InterviewType interviewType,
            LocalDateTime scheduledAt,
            InterviewStatus status,
            InterviewResult result,
            String notes) {

        this.id = id;
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.roundNumber = roundNumber;
        this.roundName = roundName;
        this.interviewType = interviewType;
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.result = result;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public String getRoundName() {
        return roundName;
    }

    public InterviewType getInterviewType() {
        return interviewType;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public InterviewResult getResult() {
        return result;
    }

    public String getNotes() {
        return notes;
    }
}
