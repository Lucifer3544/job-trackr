package com.jobtrackr.entity;

import com.jobtrackr.enums.ActivityType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "application_activities",
    indexes = {
        @Index(
            name = "idx_activity_application_date",
            columnList = "application_id, activity_date"
        )
    }
)
public class ApplicationActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Job application is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @NotNull(message = "Activity type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false, length = 30)
    private ActivityType activityType;

    @NotBlank(message = "Activity title is required")
    @Size(max = 150, message = "Title cannot exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Activity date is required")
    @Column(name = "activity_date", nullable = false)
    private LocalDateTime activityDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "system_generated", nullable = false)
    private boolean systemGenerated = false;

    protected ApplicationActivity() {
    }

    // Constructor for manual activities
    public ApplicationActivity(
            JobApplication application,
            ActivityType activityType,
            String title,
            String description,
            LocalDateTime activityDate) {

        this(
                application,
                activityType,
                title,
                description,
                activityDate,
                false
        );
    }

    // Constructor for manual or system-generated activities
    public ApplicationActivity(
            JobApplication application,
            ActivityType activityType,
            String title,
            String description,
            LocalDateTime activityDate,
            boolean systemGenerated) {

        this.application = application;
        this.activityType = activityType;
        this.title = title;
        this.description = description;
        this.activityDate = activityDate;
        this.systemGenerated = systemGenerated;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.activityDate == null) {
            this.activityDate = this.createdAt;
        }
    }

    public Long getId() {
        return id;
    }

    public JobApplication getApplication() {
        return application;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getActivityDate() {
        return activityDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isSystemGenerated() {
        return systemGenerated;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActivityDate(LocalDateTime activityDate) {
        this.activityDate = activityDate;
    }
}
