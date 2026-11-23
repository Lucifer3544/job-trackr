package com.jobtrackr.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OfferRequest {

    @NotNull(message = "Offered CTC is required")
    @DecimalMin(value = "0.01", message = "Offered CTC must be greater than zero")
    private BigDecimal offeredCtc;

    @DecimalMin(value = "0.00", message = "Fixed CTC cannot be negative")
    private BigDecimal fixedCtc;

    @DecimalMin(value = "0.00", message = "Variable CTC cannot be negative")
    private BigDecimal variableCtc;

    @DecimalMin(value = "0.00", message = "Joining bonus cannot be negative")
    private BigDecimal joiningBonus;

    @NotNull(message = "Offer date is required")
    private LocalDate offerDate;

    private LocalDate validityDate;

    private String notes;

    public BigDecimal getOfferedCtc() {
        return offeredCtc;
    }

    public void setOfferedCtc(BigDecimal offeredCtc) {
        this.offeredCtc = offeredCtc;
    }

    public BigDecimal getFixedCtc() {
        return fixedCtc;
    }

    public void setFixedCtc(BigDecimal fixedCtc) {
        this.fixedCtc = fixedCtc;
    }

    public BigDecimal getVariableCtc() {
        return variableCtc;
    }

    public void setVariableCtc(BigDecimal variableCtc) {
        this.variableCtc = variableCtc;
    }

    public BigDecimal getJoiningBonus() {
        return joiningBonus;
    }

    public void setJoiningBonus(BigDecimal joiningBonus) {
        this.joiningBonus = joiningBonus;
    }

    public LocalDate getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(LocalDate offerDate) {
        this.offerDate = offerDate;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}