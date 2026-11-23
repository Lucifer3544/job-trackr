package com.jobtrackr.service;

import com.jobtrackr.dto.InterviewResultRequest;
import com.jobtrackr.dto.InterviewRoundRequest;
import com.jobtrackr.dto.InterviewRoundResponse;

import com.jobtrackr.entity.InterviewRound;
import com.jobtrackr.entity.JobApplication;

import com.jobtrackr.enums.ActivityType;
import com.jobtrackr.enums.InterviewStatus;

import com.jobtrackr.exception.ConflictException;
import com.jobtrackr.exception.ResourceNotFoundException;

import com.jobtrackr.repository.InterviewRoundRepository;
import com.jobtrackr.repository.JobApplicationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class InterviewRoundService {

    private final InterviewRoundRepository interviewRepository;
    private final JobApplicationRepository applicationRepository;
    private final ApplicationActivityService activityService;

    public InterviewRoundService(
            InterviewRoundRepository interviewRepository,
            JobApplicationRepository applicationRepository,
            ApplicationActivityService activityService) {

        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.activityService = activityService;
    }

    // Create interview round
    @Transactional
    public InterviewRoundResponse createInterview(
            Long applicationId,
            InterviewRoundRequest request) {

        JobApplication application = getApplication(applicationId);

        if (interviewRepository.existsByApplicationIdAndRoundNumber(
                applicationId, request.getRoundNumber())) {

            throw new ConflictException(
                    "Interview round " + request.getRoundNumber()
                    + " already exists for this application.");
        }

        InterviewRound interview = new InterviewRound(
                application,
                request.getRoundNumber(),
                request.getRoundName(),
                request.getInterviewType(),
                request.getScheduledAt(),
                request.getNotes()
        );

        InterviewRound savedInterview =
                interviewRepository.save(interview);

        activityService.recordSystemActivity(
                application,
                ActivityType.INTERVIEW,
                "Interview round created",
                "Created interview round "
                + savedInterview.getRoundNumber()
                + ": " + savedInterview.getRoundName()
        );

        return mapToResponse(savedInterview);
    }

    // Get all interview rounds for an application
    public List<InterviewRoundResponse> getInterviewsByApplication(
            Long applicationId) {

        getApplication(applicationId);

        return interviewRepository
                .findByApplicationIdOrderByRoundNumberAsc(applicationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get interview round by ID
    public InterviewRoundResponse getInterviewById(Long id) {

        return mapToResponse(getInterviewEntity(id));
    }

    // Update interview round
    @Transactional
    public InterviewRoundResponse updateInterview(
            Long id,
            InterviewRoundRequest request) {

        InterviewRound interview = getInterviewEntity(id);

        if (interviewRepository.existsByApplicationIdAndRoundNumberAndIdNot(
                interview.getApplication().getId(),
                request.getRoundNumber(),
                id)) {

            throw new ConflictException(
                    "Interview round number already exists for this application.");
        }

        // Capture existing values
        Integer previousRoundNumber = interview.getRoundNumber();
        String previousRoundName = interview.getRoundName();
        var previousInterviewType = interview.getInterviewType();
        LocalDateTime previousScheduledAt = interview.getScheduledAt();
        String previousNotes = interview.getNotes();
        InterviewStatus previousStatus = interview.getStatus();

        // Check whether any requested detail has actually changed
        boolean detailsChanged =
                !Objects.equals(previousRoundNumber, request.getRoundNumber())
                || !Objects.equals(previousRoundName, request.getRoundName())
                || !Objects.equals(previousInterviewType, request.getInterviewType())
                || !Objects.equals(previousScheduledAt, request.getScheduledAt())
                || !Objects.equals(previousNotes, request.getNotes());

        // Update interview details
        interview.setRoundNumber(request.getRoundNumber());
        interview.setRoundName(request.getRoundName());
        interview.setInterviewType(request.getInterviewType());
        interview.setScheduledAt(request.getScheduledAt());
        interview.setNotes(request.getNotes());

        // Preserve existing automatic status behavior
        if (request.getScheduledAt() != null
                && interview.getStatus() == InterviewStatus.PENDING) {

            interview.setStatus(InterviewStatus.SCHEDULED);
        }

        if (request.getScheduledAt() == null
                && interview.getStatus() == InterviewStatus.SCHEDULED) {

            interview.setStatus(InterviewStatus.PENDING);
        }

        boolean statusChanged = previousStatus != interview.getStatus();

        // No changes: return without saving or creating timeline activity
        if (!detailsChanged && !statusChanged) {
            return mapToResponse(interview);
        }

        InterviewRound updatedInterview =
                interviewRepository.save(interview);

        activityService.recordSystemActivity(
                interview.getApplication(),
                ActivityType.INTERVIEW,
                "Interview details updated",
                "Updated interview round "
                + updatedInterview.getRoundNumber()
                + ": " + updatedInterview.getRoundName()
        );

        return mapToResponse(updatedInterview);
    }

    // Update interview result
    @Transactional
    public InterviewRoundResponse updateResult(
            Long id,
            InterviewResultRequest request) {

        InterviewRound interview = getInterviewEntity(id);

        var previousResult = interview.getResult();
        var newResult = request.getResult();

        if (previousResult != newResult) {
            interview.setResult(newResult);

            InterviewRound updatedInterview =
                    interviewRepository.save(interview);

            activityService.recordSystemActivity(
                    interview.getApplication(),
                    ActivityType.INTERVIEW,
                    "Interview result updated",
                    "Interview round "
                    + interview.getRoundNumber()
                    + " result changed from "
                    + previousResult + " to " + newResult
            );

            return mapToResponse(updatedInterview);
        }

        return mapToResponse(interview);
    }

    // Reschedule interview
    @Transactional
    public InterviewRoundResponse rescheduleInterview(
            Long id,
            LocalDateTime scheduledAt) {

        InterviewRound interview = getInterviewEntity(id);

        if (interview.getStatus() == InterviewStatus.COMPLETED
                || interview.getStatus() == InterviewStatus.CANCELLED) {

            throw new ConflictException(
                    "Completed or cancelled interviews cannot be rescheduled.");
        }

        LocalDateTime previousDate = interview.getScheduledAt();

        // Avoid creating an activity if nothing changes
        if (Objects.equals(previousDate, scheduledAt)
                && interview.getStatus() == InterviewStatus.RESCHEDULED) {
            return mapToResponse(interview);
        }

        interview.setScheduledAt(scheduledAt);
        interview.setStatus(InterviewStatus.RESCHEDULED);

        InterviewRound updatedInterview =
                interviewRepository.save(interview);

        activityService.recordSystemActivity(
                interview.getApplication(),
                ActivityType.INTERVIEW,
                "Interview rescheduled",
                "Interview round "
                + interview.getRoundNumber()
                + " rescheduled from "
                + previousDate + " to " + scheduledAt
        );

        return mapToResponse(updatedInterview);
    }

    // Update interview status
    @Transactional
    public InterviewRoundResponse updateStatus(
            Long id,
            InterviewStatus status) {

        InterviewRound interview = getInterviewEntity(id);

        if (interview.getStatus() == InterviewStatus.CANCELLED
                || interview.getStatus() == InterviewStatus.COMPLETED) {

            throw new ConflictException(
                    "Completed or cancelled interviews cannot change status.");
        }

        InterviewStatus previousStatus = interview.getStatus();

        if (previousStatus != status) {
            interview.setStatus(status);

            InterviewRound updatedInterview =
                    interviewRepository.save(interview);

            activityService.recordSystemActivity(
                    interview.getApplication(),
                    ActivityType.INTERVIEW,
                    "Interview status changed",
                    "Interview round "
                    + interview.getRoundNumber()
                    + " status changed from "
                    + previousStatus + " to " + status
            );

            return mapToResponse(updatedInterview);
        }

        return mapToResponse(interview);
    }

    // Delete interview round
    @Transactional
    public void deleteInterview(Long id) {

        InterviewRound interview = getInterviewEntity(id);

        activityService.recordSystemActivity(
                interview.getApplication(),
                ActivityType.INTERVIEW,
                "Interview round deleted",
                "Deleted interview round "
                + interview.getRoundNumber()
                + ": " + interview.getRoundName()
        );

        interviewRepository.delete(interview);
    }

    // Find application
    private JobApplication getApplication(Long id) {

        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with ID: " + id));
    }

    // Find interview
    private InterviewRound getInterviewEntity(Long id) {

        return interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Interview round not found with ID: " + id));
    }

    // Map entity to response DTO
    private InterviewRoundResponse mapToResponse(
            InterviewRound interview) {

        return new InterviewRoundResponse(
                interview.getId(),
                interview.getApplication().getId(),
                interview.getApplication().getCompany().getCompanyName(),
                interview.getApplication().getJobRole(),
                interview.getRoundNumber(),
                interview.getRoundName(),
                interview.getInterviewType(),
                interview.getScheduledAt(),
                interview.getStatus(),
                interview.getResult(),
                interview.getNotes()
        );
    }
}
