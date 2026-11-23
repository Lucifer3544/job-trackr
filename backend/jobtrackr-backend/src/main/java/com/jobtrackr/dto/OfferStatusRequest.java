package com.jobtrackr.dto;

import com.jobtrackr.enums.OfferStatus;
import jakarta.validation.constraints.NotNull;

public class OfferStatusRequest {

    @NotNull(message = "Offer status is required")
    private OfferStatus status;

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }
}