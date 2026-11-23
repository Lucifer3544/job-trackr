package com.jobtrackr.service;

import com.jobtrackr.dto.DashboardResponse;
import com.jobtrackr.entity.*;
import com.jobtrackr.enums.*;
import com.jobtrackr.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final JobApplicationRepository jobApplicationRepository;
    private final InterviewRoundRepository interviewRoundRepository;
    private final OfferRepository offerRepository;
    private final FollowUpRepository followUpRepository;
    private final ApplicationActivityRepository activityRepository;

    public DashboardService(
            JobApplicationRepository jobApplicationRepository,
            InterviewRoundRepository interviewRoundRepository,
            OfferRepository offerRepository,
            FollowUpRepository followUpRepository,
            ApplicationActivityRepository activityRepository) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.interviewRoundRepository = interviewRoundRepository;
        this.offerRepository = offerRepository;
        this.followUpRepository = followUpRepository;
        this.activityRepository = activityRepository;
    }

    public DashboardResponse getDashboard() {

        List<JobApplication> applications =
                jobApplicationRepository.findAll();

        List<InterviewRound> interviews =
                interviewRoundRepository.findAll();

        List<Offer> offers =
                offerRepository.findAll();

        List<FollowUp> followUps =
                followUpRepository.findAll();

        Map<ApplicationStatus, Long> applicationsByStatus =
                getApplicationStatusCounts(applications);

        List<DashboardResponse.CompanyApplicationCount> applicationsByCompany =
                getApplicationsByCompany(applications);

        DashboardResponse.InterviewStatistics interviewStatistics =
                getInterviewStatistics(interviews);

        DashboardResponse.OfferStatistics offerStatistics =
                getOfferStatistics(offers);

        DashboardResponse.FollowUpStatistics followUpStatistics =
                getFollowUpStatistics(followUps);

        List<DashboardResponse.UpcomingInterview> upcomingInterviews =
                getUpcomingInterviews();

        List<DashboardResponse.UpcomingFollowUp> upcomingFollowUps =
                getUpcomingFollowUps();

        List<DashboardResponse.RecentActivity> recentActivities =
                getRecentActivities();

        return new DashboardResponse(
                applications.size(),
                applicationsByStatus,
                applicationsByCompany,
                interviewStatistics,
                offerStatistics,
                followUpStatistics,
                upcomingInterviews,
                upcomingFollowUps,
                recentActivities
        );
    }

    private Map<ApplicationStatus, Long> getApplicationStatusCounts(
            List<JobApplication> applications) {

        Map<ApplicationStatus, Long> counts =
                new EnumMap<>(ApplicationStatus.class);

        for (ApplicationStatus status : ApplicationStatus.values()) {
            counts.put(status, 0L);
        }

        Map<ApplicationStatus, Long> actualCounts =
                applications.stream()
                        .collect(Collectors.groupingBy(
                                JobApplication::getStatus,
                                () -> new EnumMap<>(ApplicationStatus.class),
                                Collectors.counting()
                        ));

        counts.putAll(actualCounts);

        return counts;
    }

    private List<DashboardResponse.CompanyApplicationCount> getApplicationsByCompany(
            List<JobApplication> applications) {

        Map<Company, Long> counts =
                applications.stream()
                        .collect(Collectors.groupingBy(
                                JobApplication::getCompany,
                                Collectors.counting()
                        ));

        return counts.entrySet().stream()
                .map(entry -> new DashboardResponse.CompanyApplicationCount(
                        entry.getKey().getId(),
                        entry.getKey().getCompanyName(),
                        entry.getValue()
                ))
                .sorted(
                        Comparator.comparingLong(
                                DashboardResponse.CompanyApplicationCount::getApplicationCount
                        ).reversed()
                )
                .collect(Collectors.toList());
    }

    private DashboardResponse.InterviewStatistics getInterviewStatistics(
            List<InterviewRound> interviews) {

        long total = interviews.size();

        long upcoming = interviews.stream()
                .filter(interview ->
                        interview.getScheduledAt() != null
                        && !interview.getScheduledAt().isBefore(LocalDateTime.now())
                        && (interview.getStatus() == InterviewStatus.SCHEDULED
                        || interview.getStatus() == InterviewStatus.RESCHEDULED))
                .count();

        long completed = interviews.stream()
                .filter(interview ->
                        interview.getStatus() == InterviewStatus.COMPLETED)
                .count();

        long cancelled = interviews.stream()
                .filter(interview ->
                        interview.getStatus() == InterviewStatus.CANCELLED)
                .count();

        return new DashboardResponse.InterviewStatistics(
                total, upcoming, completed, cancelled);
    }

    private DashboardResponse.OfferStatistics getOfferStatistics(
            List<Offer> offers) {

        long total = offers.size();

        long pending = countOffersByStatus(offers, OfferStatus.PENDING);
        long superseded = countOffersByStatus(offers, OfferStatus.SUPERSEDED);
        long accepted = countOffersByStatus(offers, OfferStatus.ACCEPTED);
        long rejected = countOffersByStatus(offers, OfferStatus.REJECTED);

        return new DashboardResponse.OfferStatistics(
                total, pending, superseded, accepted, rejected);
    }

    private long countOffersByStatus(
            List<Offer> offers,
            OfferStatus status) {

        return offers.stream()
                .filter(offer -> offer.getStatus() == status)
                .count();
    }

    private DashboardResponse.FollowUpStatistics getFollowUpStatistics(
            List<FollowUp> followUps) {

        long total = followUps.size();

        long pending = countFollowUpsByStatus(followUps, FollowUpStatus.PENDING);
        long completed = countFollowUpsByStatus(followUps, FollowUpStatus.COMPLETED);
        long cancelled = countFollowUpsByStatus(followUps, FollowUpStatus.CANCELLED);

        return new DashboardResponse.FollowUpStatistics(
                total, pending, completed, cancelled);
    }

    private long countFollowUpsByStatus(
            List<FollowUp> followUps,
            FollowUpStatus status) {

        return followUps.stream()
                .filter(followUp -> followUp.getStatus() == status)
                .count();
    }

    private List<DashboardResponse.UpcomingInterview> getUpcomingInterviews() {

        return interviewRoundRepository
                .findTop5ByScheduledAtGreaterThanEqualAndStatusInOrderByScheduledAtAsc(
                        LocalDateTime.now(),
                        List.of(
                                InterviewStatus.SCHEDULED,
                                InterviewStatus.RESCHEDULED
                        )
                )
                .stream()
                .map(interview -> {
                    JobApplication application = interview.getApplication();

                    return new DashboardResponse.UpcomingInterview(
                            interview.getId(),
                            application.getId(),
                            application.getCompany().getCompanyName(),
                            application.getJobRole(),
                            interview.getRoundNumber(),
                            interview.getRoundName(),
                            interview.getInterviewType(),
                            interview.getScheduledAt(),
                            interview.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    private List<DashboardResponse.UpcomingFollowUp> getUpcomingFollowUps() {

        return followUpRepository
                .findTop5ByFollowUpDateGreaterThanEqualAndStatusOrderByFollowUpDateAsc(
                        LocalDate.now(),
                        FollowUpStatus.PENDING
                )
                .stream()
                .map(followUp -> {
                    JobApplication application = followUp.getApplication();

                    return new DashboardResponse.UpcomingFollowUp(
                            followUp.getId(),
                            application.getId(),
                            application.getCompany().getCompanyName(),
                            application.getJobRole(),
                            followUp.getFollowUpDate(),
                            followUp.getFollowUpType(),
                            followUp.getDescription(),
                            followUp.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    private List<DashboardResponse.RecentActivity> getRecentActivities() {

        return activityRepository
                .findTop10ByOrderByActivityDateDesc()
                .stream()
                .map(activity -> {
                    JobApplication application = activity.getApplication();

                    return new DashboardResponse.RecentActivity(
                            activity.getId(),
                            application.getId(),
                            application.getCompany().getCompanyName(),
                            application.getJobRole(),
                            activity.getActivityType(),
                            activity.getTitle(),
                            activity.getDescription(),
                            activity.getActivityDate()
                    );
                })
                .collect(Collectors.toList());
    }
}
