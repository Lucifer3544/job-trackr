package com.jobtrackr.dto;

import com.jobtrackr.enums.FollowUpType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class FollowUpRequest {

    private Long hrContactId;

    @NotNull(message = "Follow-up date is required")
    private LocalDate followUpDate;

    @NotNull(message = "Follow-up type is required")
    private FollowUpType followUpType;

    @NotBlank(message = "Follow-up description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private String notes;

    public Long getHrContactId() {
        return hrContactId;
    }

    public void setHrContactId(Long hrContactId) {
        this.hrContactId = hrContactId;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public FollowUpType getFollowUpType() {
        return followUpType;
    }

    public void setFollowUpType(FollowUpType followUpType) {
        this.followUpType = followUpType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}