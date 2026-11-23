package com.jobtrackr.dto;

import com.jobtrackr.enums.InterviewType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class InterviewRoundRequest {

    @NotNull(message = "Round number is required")
    @Positive(message = "Round number must be positive")
    private Integer roundNumber;

    @NotBlank(message = "Round name is required")
    private String roundName;

    @NotNull(message = "Interview type is required")
    private InterviewType interviewType;

    private LocalDateTime scheduledAt;

    private String notes;

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

    public String getNotes() {
        return notes;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public void setInterviewType(InterviewType interviewType) {
        this.interviewType = interviewType;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
