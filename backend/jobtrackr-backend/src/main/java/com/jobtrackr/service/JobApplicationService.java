package com.jobtrackr.service;

import com.jobtrackr.dto.JobApplicationResponse;
import com.jobtrackr.entity.Company;
import com.jobtrackr.entity.JobApplication;
import com.jobtrackr.enums.ActivityType;
import com.jobtrackr.enums.ApplicationStatus;
import com.jobtrackr.exception.ResourceNotFoundException;
import com.jobtrackr.repository.ApplicationActivityRepository;
import com.jobtrackr.repository.ApplicationHrContactRepository;
import com.jobtrackr.repository.CompanyRepository;
import com.jobtrackr.repository.FollowUpRepository;
import com.jobtrackr.repository.InterviewRoundRepository;
import com.jobtrackr.repository.JobApplicationRepository;
import com.jobtrackr.repository.OfferRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationHrContactRepository applicationHrContactRepository;
    private final InterviewRoundRepository interviewRoundRepository;
    private final OfferRepository offerRepository;
    private final FollowUpRepository followUpRepository;
    private final ApplicationActivityRepository activityRepository;
    private final ApplicationActivityService activityService;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            CompanyRepository companyRepository,
            ApplicationHrContactRepository applicationHrContactRepository,
            InterviewRoundRepository interviewRoundRepository,
            OfferRepository offerRepository,
            FollowUpRepository followUpRepository,
            ApplicationActivityRepository activityRepository,
            ApplicationActivityService activityService) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.companyRepository = companyRepository;
        this.applicationHrContactRepository = applicationHrContactRepository;
        this.interviewRoundRepository = interviewRoundRepository;
        this.offerRepository = offerRepository;
        this.followUpRepository = followUpRepository;
        this.activityRepository = activityRepository;
        this.activityService = activityService;
    }

    // Create job application
    @Transactional
    public JobApplicationResponse createJobApplication(
            Long companyId,
            String jobRole,
            String jobLocation) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with ID: " + companyId
                        )
                );

        JobApplication application =
                new JobApplication(company, jobRole, jobLocation);

        JobApplication savedApplication =
                jobApplicationRepository.save(application);

        activityService.recordSystemActivity(
                savedApplication,
                ActivityType.APPLICATION,
                "Application created",
                "Application created for " + savedApplication.getJobRole()
        );

        return mapToResponse(savedApplication);
    }

    // Get all applications
    public List<JobApplicationResponse> getAllApplications() {

        return jobApplicationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get application by ID
    public JobApplicationResponse getApplicationById(Long id) {

        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with ID: " + id
                        )
                );

        return mapToResponse(application);
    }

    // Get applications by company
    public List<JobApplicationResponse> getApplicationsByCompany(
            Long companyId) {

        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException(
                    "Company not found with ID: " + companyId
            );
        }

        return jobApplicationRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Update application status
    @Transactional
    public JobApplicationResponse updateApplicationStatus(
            Long id,
            ApplicationStatus status) {

        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with ID: " + id
                        )
                );

        ApplicationStatus previousStatus = application.getStatus();

        application.setStatus(status);

        JobApplication updatedApplication =
                jobApplicationRepository.save(application);

        if (previousStatus != status) {
            activityService.recordSystemActivity(
                    updatedApplication,
                    ActivityType.STATUS_CHANGE,
                    "Application status changed",
                    "Status changed from " + previousStatus
                            + " to " + status
            );
        }

        return mapToResponse(updatedApplication);
    }

    // Update application details
    @Transactional
    public JobApplicationResponse updateApplication(
            Long id,
            String jobRole,
            String jobLocation) {

        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with ID: " + id
                        )
                );

        application.setJobRole(jobRole);
        application.setJobLocation(jobLocation);

        JobApplication updatedApplication =
                jobApplicationRepository.save(application);

        return mapToResponse(updatedApplication);
    }

    // Delete application and all dependent records
    @Transactional
    public void deleteApplication(Long id) {

        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with ID: " + id
                        )
                );

        // Delete HR contact associations
        applicationHrContactRepository.deleteByApplicationId(id);

        // Delete interview rounds
        interviewRoundRepository
                .deleteAll(
                        interviewRoundRepository
                                .findByApplicationIdOrderByRoundNumberAsc(id)
                );

        // Delete offers
        offerRepository
                .deleteAll(
                        offerRepository
                                .findByApplicationIdOrderByRevisionNumberAsc(id)
                );

        // Delete follow-ups
        followUpRepository
                .deleteAll(
                        followUpRepository
                                .findByApplicationIdOrderByFollowUpDateAsc(id)
                );

        // Delete application activity timeline records
        activityRepository
                .deleteAll(
                        activityRepository
                                .findByApplicationIdOrderByActivityDateDesc(id)
                );

        // Finally delete the job application
        jobApplicationRepository.delete(application);
    }

    // Map entity to response DTO
    private JobApplicationResponse mapToResponse(
            JobApplication application) {

        return new JobApplicationResponse(
                application.getId(),
                application.getCompany().getId(),
                application.getCompany().getCompanyName(),
                application.getJobRole(),
                application.getJobLocation(),
                application.getStatus()
        );
    }
}
