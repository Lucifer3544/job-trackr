package com.jobtrackr.service;

import com.jobtrackr.dto.FollowUpRequest;
import com.jobtrackr.dto.FollowUpResponse;
import com.jobtrackr.entity.FollowUp;
import com.jobtrackr.entity.HRContact;
import com.jobtrackr.entity.JobApplication;
import com.jobtrackr.enums.ActivityType;
import com.jobtrackr.enums.FollowUpStatus;
import com.jobtrackr.exception.ConflictException;
import com.jobtrackr.exception.ResourceNotFoundException;
import com.jobtrackr.repository.FollowUpRepository;
import com.jobtrackr.repository.HRContactRepository;
import com.jobtrackr.repository.JobApplicationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final HRContactRepository hrContactRepository;
    private final ApplicationActivityService activityService;

    public FollowUpService(
            FollowUpRepository followUpRepository,
            JobApplicationRepository jobApplicationRepository,
            HRContactRepository hrContactRepository,
            ApplicationActivityService activityService) {

        this.followUpRepository = followUpRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.hrContactRepository = hrContactRepository;
        this.activityService = activityService;
    }

    // Create a follow-up for a job application
    @Transactional
    public FollowUpResponse createFollowUp(
            Long applicationId,
            FollowUpRequest request) {

        JobApplication application = getApplication(applicationId);

        HRContact hrContact = getAndValidateHrContact(
                request.getHrContactId(),
                application
        );

        FollowUp followUp = new FollowUp(
                application,
                hrContact,
                request.getFollowUpDate(),
                request.getFollowUpType(),
                request.getDescription().trim(),
                request.getNotes()
        );

        FollowUp savedFollowUp = followUpRepository.save(followUp);

        activityService.recordSystemActivity(
                application,
                ActivityType.FOLLOW_UP,
                "Follow-up created",
                "Created " + savedFollowUp.getFollowUpType()
                + " follow-up: " + savedFollowUp.getDescription()
        );

        return mapToResponse(savedFollowUp);
    }

    // Get all follow-ups for an application
    public List<FollowUpResponse> getFollowUpsByApplication(Long applicationId) {

        if (!jobApplicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException(
                    "Job application not found with ID: " + applicationId
            );
        }

        return followUpRepository
                .findByApplicationIdOrderByFollowUpDateAsc(applicationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get all follow-ups by status
    public List<FollowUpResponse> getFollowUpsByStatus(FollowUpStatus status) {

        return followUpRepository
                .findByStatusOrderByFollowUpDateAsc(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get a specific follow-up
    public FollowUpResponse getFollowUpById(Long id) {

        return mapToResponse(getFollowUpEntity(id));
    }

    // Update a pending follow-up
    @Transactional
    public FollowUpResponse updateFollowUp(
            Long id,
            FollowUpRequest request) {

        FollowUp followUp = getFollowUpEntity(id);

        ensurePending(followUp);

        HRContact hrContact = getAndValidateHrContact(
                request.getHrContactId(),
                followUp.getApplication()
        );

        followUp.setHrContact(hrContact);
        followUp.setFollowUpDate(request.getFollowUpDate());
        followUp.setFollowUpType(request.getFollowUpType());
        followUp.setDescription(request.getDescription().trim());
        followUp.setNotes(request.getNotes());

        FollowUp updatedFollowUp = followUpRepository.save(followUp);

        activityService.recordSystemActivity(
                followUp.getApplication(),
                ActivityType.FOLLOW_UP,
                "Follow-up details updated",
                "Updated follow-up: " + updatedFollowUp.getDescription()
        );

        return mapToResponse(updatedFollowUp);
    }

    // Complete or cancel a pending follow-up
    @Transactional
    public FollowUpResponse updateFollowUpStatus(
            Long id,
            FollowUpStatus newStatus) {

        FollowUp followUp = getFollowUpEntity(id);

        ensurePending(followUp);

        if (newStatus == null) {
            throw new ConflictException("Follow-up status is required.");
        }

        if (newStatus != FollowUpStatus.COMPLETED
                && newStatus != FollowUpStatus.CANCELLED) {

            throw new ConflictException(
                    "A pending follow-up can only be changed to COMPLETED or CANCELLED."
            );
        }

        followUp.setStatus(newStatus);

        FollowUp updatedFollowUp = followUpRepository.save(followUp);

        activityService.recordSystemActivity(
                followUp.getApplication(),
                ActivityType.FOLLOW_UP,
                "Follow-up " + newStatus.name().toLowerCase(),
                "Follow-up " + followUp.getFollowUpType()
                + " was " + newStatus.name().toLowerCase()
                + ": " + followUp.getDescription()
        );

        return mapToResponse(updatedFollowUp);
    }

    // Delete a follow-up
    @Transactional
    public void deleteFollowUp(Long id) {

        FollowUp followUp = getFollowUpEntity(id);

        activityService.recordSystemActivity(
                followUp.getApplication(),
                ActivityType.FOLLOW_UP,
                "Follow-up deleted",
                "Deleted follow-up: " + followUp.getDescription()
        );

        followUpRepository.delete(followUp);
    }

    private JobApplication getApplication(Long applicationId) {

        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job application not found with ID: " + applicationId
                ));
    }

    private HRContact getAndValidateHrContact(
            Long hrContactId,
            JobApplication application) {

        if (hrContactId == null) {
            return null;
        }

        HRContact hrContact = hrContactRepository.findById(hrContactId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "HR contact not found with ID: " + hrContactId
                ));

        if (!hrContact.getCompany().getId()
                .equals(application.getCompany().getId())) {

            throw new ConflictException(
                    "The selected HR contact does not belong to the same company as the job application."
            );
        }

        return hrContact;
    }

    private FollowUp getFollowUpEntity(Long id) {

        return followUpRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Follow-up not found with ID: " + id
                ));
    }

    private void ensurePending(FollowUp followUp) {

        if (followUp.getStatus() != FollowUpStatus.PENDING) {
            throw new ConflictException(
                    "Only pending follow-ups can be updated. Current status: "
                            + followUp.getStatus()
            );
        }
    }

    private FollowUpResponse mapToResponse(FollowUp followUp) {

        HRContact hrContact = followUp.getHrContact();

        return new FollowUpResponse(
                followUp.getId(),
                followUp.getApplication().getId(),
                followUp.getApplication().getCompany().getCompanyName(),
                followUp.getApplication().getJobRole(),
                hrContact != null ? hrContact.getId() : null,
                followUp.getFollowUpDate(),
                followUp.getFollowUpType(),
                followUp.getDescription(),
                followUp.getStatus(),
                followUp.getNotes()
        );
    }
}
