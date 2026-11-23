package com.jobtrackr.dto;

import com.jobtrackr.enums.FollowUpStatus;
import jakarta.validation.constraints.NotNull;

public class FollowUpStatusRequest {

    @NotNull(message = "Follow-up status is required")
    private FollowUpStatus status;

    public FollowUpStatus getStatus() {
        return status;
    }

    public void setStatus(FollowUpStatus status) {
        this.status = status;
    }
}