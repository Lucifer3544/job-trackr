package com.jobtrackr.entity;

import com.jobtrackr.enums.FollowUpStatus;
import com.jobtrackr.enums.FollowUpType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(
    name = "follow_ups",
    indexes = {
        @Index(name = "idx_followup_application", columnList = "application_id"),
        @Index(name = "idx_followup_date", columnList = "follow_up_date")
    }
)
public class FollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Job application is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_contact_id")
    private HRContact hrContact;

    @NotNull(message = "Follow-up date is required")
    @Column(name = "follow_up_date", nullable = false)
    private LocalDate followUpDate;

    @NotNull(message = "Follow-up type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "follow_up_type", nullable = false, length = 20)
    private FollowUpType followUpType;

    @NotBlank(message = "Follow-up description is required")
    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FollowUpStatus status = FollowUpStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    protected FollowUp() {
    }

    public FollowUp(
            JobApplication application,
            HRContact hrContact,
            LocalDate followUpDate,
            FollowUpType followUpType,
            String description,
            String notes) {

        this.application = application;
        this.hrContact = hrContact;
        this.followUpDate = followUpDate;
        this.followUpType = followUpType;
        this.description = description;
        this.notes = notes;
        this.status = FollowUpStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public JobApplication getApplication() {
        return application;
    }

    public HRContact getHrContact() {
        return hrContact;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public FollowUpType getFollowUpType() {
        return followUpType;
    }

    public String getDescription() {
        return description;
    }

    public FollowUpStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public void setHrContact(HRContact hrContact) {
        this.hrContact = hrContact;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }

    public void setFollowUpType(FollowUpType followUpType) {
        this.followUpType = followUpType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(FollowUpStatus status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}