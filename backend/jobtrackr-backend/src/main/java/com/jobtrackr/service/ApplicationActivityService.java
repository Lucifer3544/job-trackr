
package com.jobtrackr.service;

import com.jobtrackr.dto.ApplicationActivityRequest;
import com.jobtrackr.dto.ApplicationActivityResponse;
import com.jobtrackr.entity.ApplicationActivity;
import com.jobtrackr.entity.JobApplication;
import com.jobtrackr.enums.ActivityType;
import com.jobtrackr.exception.ConflictException;
import com.jobtrackr.exception.ResourceNotFoundException;
import com.jobtrackr.repository.ApplicationActivityRepository;
import com.jobtrackr.repository.JobApplicationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ApplicationActivityService {

    private final ApplicationActivityRepository activityRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public ApplicationActivityService(
            ApplicationActivityRepository activityRepository,
            JobApplicationRepository jobApplicationRepository) {

        this.activityRepository = activityRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    // Create a manual activity
    @Transactional
    public ApplicationActivityResponse createActivity(
            Long applicationId,
            ApplicationActivityRequest request) {

        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job application not found with ID: " + applicationId
                ));

        LocalDateTime activityDate = request.getActivityDate() != null
                ? request.getActivityDate()
                : LocalDateTime.now();

        ApplicationActivity activity = new ApplicationActivity(
                application,
                request.getActivityType(),
                request.getTitle().trim(),
                request.getDescription(),
                activityDate
        );

        return mapToResponse(activityRepository.save(activity));
    }

    // Record an automatically generated activity
    @Transactional
    public void recordSystemActivity(
            JobApplication application,
            ActivityType activityType,
            String title,
            String description) {

        ApplicationActivity activity = new ApplicationActivity(
                application,
                activityType,
                title,
                description,
                LocalDateTime.now(),
                true
        );

        activityRepository.save(activity);
    }

    // Get all activities for an application
    public List<ApplicationActivityResponse> getActivitiesByApplication(
            Long applicationId) {

        if (!jobApplicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException(
                    "Job application not found with ID: " + applicationId
            );
        }

        return activityRepository
                .findByApplicationIdOrderByActivityDateDesc(applicationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Filter activities by type
    public List<ApplicationActivityResponse> getActivitiesByType(
            Long applicationId,
            ActivityType activityType) {

        if (!jobApplicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException(
                    "Job application not found with ID: " + applicationId
            );
        }

        return activityRepository
                .findByApplicationIdAndActivityTypeOrderByActivityDateDesc(
                        applicationId,
                        activityType
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get one activity
    public ApplicationActivityResponse getActivityById(Long id) {
        return mapToResponse(getActivityEntity(id));
    }

    // Update a manual activity
    @Transactional
    public ApplicationActivityResponse updateActivity(
            Long id,
            ApplicationActivityRequest request) {

        ApplicationActivity activity = getActivityEntity(id);

        if (activity.isSystemGenerated()) {
            throw new ConflictException(
                    "Automatically generated activities cannot be updated."
            );
        }

        activity.setActivityType(request.getActivityType());
        activity.setTitle(request.getTitle().trim());
        activity.setDescription(request.getDescription());

        if (request.getActivityDate() != null) {
            activity.setActivityDate(request.getActivityDate());
        }

        return mapToResponse(activityRepository.save(activity));
    }

    // Delete a manual activity
    @Transactional
    public void deleteActivity(Long id) {

        ApplicationActivity activity = getActivityEntity(id);

        if (activity.isSystemGenerated()) {
            throw new ConflictException(
                    "Automatically generated activities cannot be deleted."
            );
        }

        activityRepository.delete(activity);
    }

    private ApplicationActivity getActivityEntity(Long id) {

        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application activity not found with ID: " + id
                ));
    }

    private ApplicationActivityResponse mapToResponse(
            ApplicationActivity activity) {

        JobApplication application = activity.getApplication();

        return new ApplicationActivityResponse(
                activity.getId(),
                application.getId(),
                application.getCompany().getCompanyName(),
                application.getJobRole(),
                activity.getActivityType(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getActivityDate(),
                activity.getCreatedAt()
        );
    }
}
