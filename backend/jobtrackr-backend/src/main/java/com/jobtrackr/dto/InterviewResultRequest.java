package com.jobtrackr.dto;

import com.jobtrackr.enums.InterviewResult;
import jakarta.validation.constraints.NotNull;

public class InterviewResultRequest {

    @NotNull(message = "Interview result is required")
    private InterviewResult result;

    public InterviewResult getResult() {
        return result;
    }

    public void setResult(InterviewResult result) {
        this.result = result;
    }
}
