package com.jobtrackr.entity;

import com.jobtrackr.enums.OfferStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "offers",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_offer_application_revision",
            columnNames = {"application_id", "revision_number"}
        )
    },
    indexes = {
        @Index(name = "idx_offer_application", columnList = "application_id")
    }
)
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Job application is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @NotNull(message = "Revision number is required")
    @Min(value = 1, message = "Revision number must be at least 1")
    @Column(name = "revision_number", nullable = false)
    private Integer revisionNumber;

    @NotNull(message = "Offered CTC is required")
    @DecimalMin(value = "0.01", message = "Offered CTC must be greater than zero")
    @Column(name = "offered_ctc", nullable = false, precision = 12, scale = 2)
    private BigDecimal offeredCtc;

    @DecimalMin(value = "0.00", message = "Fixed CTC cannot be negative")
    @Column(name = "fixed_ctc", precision = 12, scale = 2)
    private BigDecimal fixedCtc;

    @DecimalMin(value = "0.00", message = "Variable CTC cannot be negative")
    @Column(name = "variable_ctc", precision = 12, scale = 2)
    private BigDecimal variableCtc;

    @DecimalMin(value = "0.00", message = "Joining bonus cannot be negative")
    @Column(name = "joining_bonus", precision = 12, scale = 2)
    private BigDecimal joiningBonus;

    @NotNull(message = "Offer date is required")
    @Column(name = "offer_date", nullable = false)
    private LocalDate offerDate;

    @Column(name = "validity_date")
    private LocalDate validityDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OfferStatus status = OfferStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    protected Offer() {
    }

    public Offer(
            JobApplication application,
            Integer revisionNumber,
            BigDecimal offeredCtc,
            BigDecimal fixedCtc,
            BigDecimal variableCtc,
            BigDecimal joiningBonus,
            LocalDate offerDate,
            LocalDate validityDate,
            String notes) {

        this.application = application;
        this.revisionNumber = revisionNumber;
        this.offeredCtc = offeredCtc;
        this.fixedCtc = fixedCtc;
        this.variableCtc = variableCtc;
        this.joiningBonus = joiningBonus;
        this.offerDate = offerDate;
        this.validityDate = validityDate;
        this.status = OfferStatus.PENDING;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public JobApplication getApplication() {
        return application;
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public BigDecimal getOfferedCtc() {
        return offeredCtc;
    }

    public BigDecimal getFixedCtc() {
        return fixedCtc;
    }

    public BigDecimal getVariableCtc() {
        return variableCtc;
    }

    public BigDecimal getJoiningBonus() {
        return joiningBonus;
    }

    public LocalDate getOfferDate() {
        return offerDate;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setOfferedCtc(BigDecimal offeredCtc) {
        this.offeredCtc = offeredCtc;
    }

    public void setFixedCtc(BigDecimal fixedCtc) {
        this.fixedCtc = fixedCtc;
    }

    public void setVariableCtc(BigDecimal variableCtc) {
        this.variableCtc = variableCtc;
    }

    public void setJoiningBonus(BigDecimal joiningBonus) {
        this.joiningBonus = joiningBonus;
    }

    public void setOfferDate(LocalDate offerDate) {
        this.offerDate = offerDate;
    }

    public void setValidityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}