package com.jobtrackr.entity;

import com.jobtrackr.enums.InterviewResult;
import com.jobtrackr.enums.InterviewStatus;
import com.jobtrackr.enums.InterviewType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "interview_rounds",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_application_round_number",
            columnNames = {"application_id", "round_number"}
        )
    },
    indexes = {
        @Index(
            name = "idx_interview_application",
            columnList = "application_id"
        ),
        @Index(
            name = "idx_interview_scheduled_at",
            columnList = "scheduled_at"
        )
    }
)
public class InterviewRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Application is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @NotNull(message = "Round number is required")
    @Positive(message = "Round number must be positive")
    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @NotBlank(message = "Round name is required")
    @Column(name = "round_name", nullable = false)
    private String roundName;

    @NotNull(message = "Interview type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "interview_type", nullable = false)
    private InterviewType interviewType;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Interview status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status = InterviewStatus.PENDING;

    @NotNull(message = "Interview result is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewResult result = InterviewResult.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    protected InterviewRound() {
    }

    public InterviewRound(
            JobApplication application,
            Integer roundNumber,
            String roundName,
            InterviewType interviewType,
            LocalDateTime scheduledAt,
            String notes) {

        this.application = application;
        this.roundNumber = roundNumber;
        this.roundName = roundName;
        this.interviewType = interviewType;
        this.scheduledAt = scheduledAt;
        this.notes = notes;

        this.status = scheduledAt == null
                ? InterviewStatus.PENDING
                : InterviewStatus.SCHEDULED;

        this.result = InterviewResult.PENDING;
    }

    public Long getId() {
        return id;
    }

    public JobApplication getApplication() {
        return application;
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

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public void setResult(InterviewResult result) {
        this.result = result;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
