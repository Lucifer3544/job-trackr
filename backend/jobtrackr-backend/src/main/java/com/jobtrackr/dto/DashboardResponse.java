package com.jobtrackr.dto;

import com.jobtrackr.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class DashboardResponse {

    private long totalApplications;
    private Map<ApplicationStatus, Long> applicationsByStatus;
    private List<CompanyApplicationCount> applicationsByCompany;

    private InterviewStatistics interviewStatistics;
    private OfferStatistics offerStatistics;
    private FollowUpStatistics followUpStatistics;

    private List<UpcomingInterview> upcomingInterviews;
    private List<UpcomingFollowUp> upcomingFollowUps;
    private List<RecentActivity> recentActivities;

    public DashboardResponse(
            long totalApplications,
            Map<ApplicationStatus, Long> applicationsByStatus,
            List<CompanyApplicationCount> applicationsByCompany,
            InterviewStatistics interviewStatistics,
            OfferStatistics offerStatistics,
            FollowUpStatistics followUpStatistics,
            List<UpcomingInterview> upcomingInterviews,
            List<UpcomingFollowUp> upcomingFollowUps,
            List<RecentActivity> recentActivities) {

        this.totalApplications = totalApplications;
        this.applicationsByStatus = applicationsByStatus;
        this.applicationsByCompany = applicationsByCompany;
        this.interviewStatistics = interviewStatistics;
        this.offerStatistics = offerStatistics;
        this.followUpStatistics = followUpStatistics;
        this.upcomingInterviews = upcomingInterviews;
        this.upcomingFollowUps = upcomingFollowUps;
        this.recentActivities = recentActivities;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public Map<ApplicationStatus, Long> getApplicationsByStatus() {
        return applicationsByStatus;
    }

    public List<CompanyApplicationCount> getApplicationsByCompany() {
        return applicationsByCompany;
    }

    public InterviewStatistics getInterviewStatistics() {
        return interviewStatistics;
    }

    public OfferStatistics getOfferStatistics() {
        return offerStatistics;
    }

    public FollowUpStatistics getFollowUpStatistics() {
        return followUpStatistics;
    }

    public List<UpcomingInterview> getUpcomingInterviews() {
        return upcomingInterviews;
    }

    public List<UpcomingFollowUp> getUpcomingFollowUps() {
        return upcomingFollowUps;
    }

    public List<RecentActivity> getRecentActivities() {
        return recentActivities;
    }

    public static class CompanyApplicationCount {

        private Long companyId;
        private String companyName;
        private long applicationCount;

        public CompanyApplicationCount(
                Long companyId,
                String companyName,
                long applicationCount) {

            this.companyId = companyId;
            this.companyName = companyName;
            this.applicationCount = applicationCount;
        }

        public Long getCompanyId() {
            return companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public long getApplicationCount() {
            return applicationCount;
        }
    }

    public static class InterviewStatistics {

        private long total;
        private long upcoming;
        private long completed;
        private long cancelled;

        public InterviewStatistics(
                long total,
                long upcoming,
                long completed,
                long cancelled) {

            this.total = total;
            this.upcoming = upcoming;
            this.completed = completed;
            this.cancelled = cancelled;
        }

        public long getTotal() {
            return total;
        }

        public long getUpcoming() {
            return upcoming;
        }

        public long getCompleted() {
            return completed;
        }

        public long getCancelled() {
            return cancelled;
        }
    }

    public static class OfferStatistics {

        private long total;
        private long pending;
        private long superseded;
        private long accepted;
        private long rejected;

        public OfferStatistics(
                long total,
                long pending,
                long superseded,
                long accepted,
                long rejected) {

            this.total = total;
            this.pending = pending;
            this.superseded = superseded;
            this.accepted = accepted;
            this.rejected = rejected;
        }

        public long getTotal() {
            return total;
        }

        public long getPending() {
            return pending;
        }

        public long getSuperseded() {
            return superseded;
        }

        public long getAccepted() {
            return accepted;
        }

        public long getRejected() {
            return rejected;
        }
    }

    public static class FollowUpStatistics {

        private long total;
        private long pending;
        private long completed;
        private long cancelled;

        public FollowUpStatistics(
                long total,
                long pending,
                long completed,
                long cancelled) {

            this.total = total;
            this.pending = pending;
            this.completed = completed;
            this.cancelled = cancelled;
        }

        public long getTotal() {
            return total;
        }

        public long getPending() {
            return pending;
        }

        public long getCompleted() {
            return completed;
        }

        public long getCancelled() {
            return cancelled;
        }
    }

    public static class UpcomingInterview {

        private Long id;
        private Long applicationId;
        private String companyName;
        private String jobRole;
        private Integer roundNumber;
        private String roundName;
        private InterviewType interviewType;
        private LocalDateTime scheduledAt;
        private InterviewStatus status;

        public UpcomingInterview(
                Long id,
                Long applicationId,
                String companyName,
                String jobRole,
                Integer roundNumber,
                String roundName,
                InterviewType interviewType,
                LocalDateTime scheduledAt,
                InterviewStatus status) {

            this.id = id;
            this.applicationId = applicationId;
            this.companyName = companyName;
            this.jobRole = jobRole;
            this.roundNumber = roundNumber;
            this.roundName = roundName;
            this.interviewType = interviewType;
            this.scheduledAt = scheduledAt;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public Long getApplicationId() {
            return applicationId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getJobRole() {
            return jobRole;
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
    }

    public static class UpcomingFollowUp {

        private Long id;
        private Long applicationId;
        private String companyName;
        private String jobRole;
        private LocalDate followUpDate;
        private FollowUpType followUpType;
        private String description;
        private FollowUpStatus status;

        public UpcomingFollowUp(
                Long id,
                Long applicationId,
                String companyName,
                String jobRole,
                LocalDate followUpDate,
                FollowUpType followUpType,
                String description,
                FollowUpStatus status) {

            this.id = id;
            this.applicationId = applicationId;
            this.companyName = companyName;
            this.jobRole = jobRole;
            this.followUpDate = followUpDate;
            this.followUpType = followUpType;
            this.description = description;
            this.status = status;
        }

        public Long getId() {
            return id;
        }

        public Long getApplicationId() {
            return applicationId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getJobRole() {
            return jobRole;
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
    }

    public static class RecentActivity {

        private Long id;
        private Long applicationId;
        private String companyName;
        private String jobRole;
        private ActivityType activityType;
        private String title;
        private String description;
        private LocalDateTime activityDate;

        public RecentActivity(
                Long id,
                Long applicationId,
                String companyName,
                String jobRole,
                ActivityType activityType,
                String title,
                String description,
                LocalDateTime activityDate) {

            this.id = id;
            this.applicationId = applicationId;
            this.companyName = companyName;
            this.jobRole = jobRole;
            this.activityType = activityType;
            this.title = title;
            this.description = description;
            this.activityDate = activityDate;
        }

        public Long getId() {
            return id;
        }

        public Long getApplicationId() {
            return applicationId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getJobRole() {
            return jobRole;
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
    }
}
