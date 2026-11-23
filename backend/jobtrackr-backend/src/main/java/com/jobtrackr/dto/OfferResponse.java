package com.jobtrackr.dto;

import com.jobtrackr.enums.OfferStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OfferResponse {

    private Long id;
    private Long applicationId;
    private String companyName;
    private String jobRole;
    private Integer revisionNumber;
    private BigDecimal offeredCtc;
    private BigDecimal fixedCtc;
    private BigDecimal variableCtc;
    private BigDecimal joiningBonus;
    private LocalDate offerDate;
    private LocalDate validityDate;
    private OfferStatus status;
    private String notes;

    public OfferResponse(
            Long id,
            Long applicationId,
            String companyName,
            String jobRole,
            Integer revisionNumber,
            BigDecimal offeredCtc,
            BigDecimal fixedCtc,
            BigDecimal variableCtc,
            BigDecimal joiningBonus,
            LocalDate offerDate,
            LocalDate validityDate,
            OfferStatus status,
            String notes) {

        this.id = id;
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.revisionNumber = revisionNumber;
        this.offeredCtc = offeredCtc;
        this.fixedCtc = fixedCtc;
        this.variableCtc = variableCtc;
        this.joiningBonus = joiningBonus;
        this.offerDate = offerDate;
        this.validityDate = validityDate;
        this.status = status;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public Long getApplicationId() { return applicationId; }
    public String getCompanyName() { return companyName; }
    public String getJobRole() { return jobRole; }
    public Integer getRevisionNumber() { return revisionNumber; }
    public BigDecimal getOfferedCtc() { return offeredCtc; }
    public BigDecimal getFixedCtc() { return fixedCtc; }
    public BigDecimal getVariableCtc() { return variableCtc; }
    public BigDecimal getJoiningBonus() { return joiningBonus; }
    public LocalDate getOfferDate() { return offerDate; }
    public LocalDate getValidityDate() { return validityDate; }
    public OfferStatus getStatus() { return status; }
    public String getNotes() { return notes; }
}