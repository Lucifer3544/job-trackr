package com.jobtrackr.service;

import com.jobtrackr.dto.HRContactRequest;
import com.jobtrackr.dto.HRContactResponse;
import com.jobtrackr.entity.ApplicationHrContact;
import com.jobtrackr.entity.Company;
import com.jobtrackr.entity.HRContact;
import com.jobtrackr.entity.JobApplication;
import com.jobtrackr.enums.ActivityType;
import com.jobtrackr.exception.ConflictException;
import com.jobtrackr.exception.ResourceNotFoundException;
import com.jobtrackr.repository.ApplicationHrContactRepository;
import com.jobtrackr.repository.CompanyRepository;
import com.jobtrackr.repository.FollowUpRepository;
import com.jobtrackr.repository.HRContactRepository;
import com.jobtrackr.repository.JobApplicationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HRContactService {

    private final HRContactRepository hrContactRepository;
    private final CompanyRepository companyRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ApplicationHrContactRepository applicationHrContactRepository;
    private final FollowUpRepository followUpRepository;
    private final ApplicationActivityService activityService;

    public HRContactService(
            HRContactRepository hrContactRepository,
            CompanyRepository companyRepository,
            JobApplicationRepository jobApplicationRepository,
            ApplicationHrContactRepository applicationHrContactRepository,
            FollowUpRepository followUpRepository,
            ApplicationActivityService activityService) {

        this.hrContactRepository = hrContactRepository;
        this.companyRepository = companyRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.applicationHrContactRepository = applicationHrContactRepository;
        this.followUpRepository = followUpRepository;
        this.activityService = activityService;
    }

    // Create HR contact
    @Transactional
    public HRContactResponse createHRContact(
            Long companyId,
            HRContactRequest request) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with ID: " + companyId
                        )
                );

        HRContact contact = new HRContact(
                company,
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getDesignation()
        );

        return mapToResponse(hrContactRepository.save(contact));
    }

    // Get all HR contacts belonging to a company
    public List<HRContactResponse> getHRContactsByCompany(
            Long companyId) {

        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException(
                    "Company not found with ID: " + companyId
            );
        }

        return hrContactRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get HR contact by ID
    public HRContactResponse getHRContactById(Long id) {

        HRContact contact = getContactEntity(id);

        return mapToResponse(contact);
    }

    // Update HR contact
    @Transactional
    public HRContactResponse updateHRContact(
            Long id,
            HRContactRequest request) {

        HRContact contact = getContactEntity(id);

        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setDesignation(request.getDesignation());

        return mapToResponse(hrContactRepository.save(contact));
    }

    // Delete HR contact
    @Transactional
    public void deleteHRContact(Long id) {

        HRContact contact = getContactEntity(id);

        long linkedApplications =
                applicationHrContactRepository.countByHrContactId(id);

        if (linkedApplications > 0) {
            throw new ConflictException(
                    "Cannot delete HR contact. It is linked to "
                    + linkedApplications + " job application(s). "
                    + "Unlink it first."
            );
        }

        long linkedFollowUps =
                followUpRepository.countByHrContactId(id);

        if (linkedFollowUps > 0) {
            throw new ConflictException(
                    "Cannot delete HR contact. It is referenced by "
                    + linkedFollowUps + " follow-up(s). "
                    + "Update or remove those follow-ups first."
            );
        }

        hrContactRepository.delete(contact);
    }

    // Link HR contact to job application
    @Transactional
    public void linkHRContactToApplication(
            Long applicationId,
            Long hrContactId) {

        JobApplication application = jobApplicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with ID: "
                                + applicationId
                        )
                );

        HRContact contact = getContactEntity(hrContactId);

        if (!application.getCompany().getId()
                .equals(contact.getCompany().getId())) {

            throw new ConflictException(
                    "HR contact and job application must belong "
                    + "to the same company."
            );
        }

        boolean alreadyLinked =
                applicationHrContactRepository
                        .existsByApplicationIdAndHrContactId(
                                applicationId,
                                hrContactId
                        );

        if (alreadyLinked) {
            throw new ConflictException(
                    "HR contact is already linked to this application."
            );
        }

        ApplicationHrContact link =
                new ApplicationHrContact(application, contact);

        applicationHrContactRepository.save(link);

        activityService.recordSystemActivity(
                application,
                ActivityType.HR_CONTACT,
                "HR contact linked",
                "Linked HR contact: " + contact.getName()
        );
    }

    // Unlink HR contact from job application
    @Transactional
    public void unlinkHRContactFromApplication(
            Long applicationId,
            Long hrContactId) {

        ApplicationHrContact link =
                applicationHrContactRepository
                        .findByApplicationIdAndHrContactId(
                                applicationId,
                                hrContactId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "HR contact is not linked to "
                                        + "this application."
                                )
                        );

        JobApplication application = link.getApplication();
        HRContact contact = link.getHrContact();

        applicationHrContactRepository.delete(link);

        activityService.recordSystemActivity(
                application,
                ActivityType.HR_CONTACT,
                "HR contact unlinked",
                "Unlinked HR contact: " + contact.getName()
        );
    }

    // Get HR contacts linked to an application
    public List<HRContactResponse> getHRContactsByApplication(
            Long applicationId) {

        if (!jobApplicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException(
                    "Application not found with ID: " + applicationId
            );
        }

        return applicationHrContactRepository
                .findByApplicationId(applicationId)
                .stream()
                .map(link -> mapToResponse(link.getHrContact()))
                .toList();
    }

    // Find HR contact entity
    private HRContact getContactEntity(Long id) {

        return hrContactRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "HR contact not found with ID: " + id
                        )
                );
    }

    // Map entity to response DTO
    private HRContactResponse mapToResponse(HRContact contact) {

        return new HRContactResponse(
                contact.getId(),
                contact.getCompany().getId(),
                contact.getCompany().getCompanyName(),
                contact.getName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getDesignation()
        );
    }
}
