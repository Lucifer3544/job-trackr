package com.jobtrackr.service;

import com.jobtrackr.dto.OfferRequest;
import com.jobtrackr.dto.OfferResponse;
import com.jobtrackr.entity.JobApplication;
import com.jobtrackr.entity.Offer;
import com.jobtrackr.enums.ActivityType;
import com.jobtrackr.enums.OfferStatus;
import com.jobtrackr.exception.ConflictException;
import com.jobtrackr.exception.ResourceNotFoundException;
import com.jobtrackr.repository.JobApplicationRepository;
import com.jobtrackr.repository.OfferRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OfferService {

    private final OfferRepository offerRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ApplicationActivityService activityService;

    public OfferService(
            OfferRepository offerRepository,
            JobApplicationRepository jobApplicationRepository,
            ApplicationActivityService activityService) {

        this.offerRepository = offerRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.activityService = activityService;
    }

    // Create a new company offer or revision
    @Transactional
    public OfferResponse createOffer(Long applicationId, OfferRequest request) {

        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job application not found with ID: " + applicationId
                ));

        validateOfferDates(request);

        boolean acceptedOfferExists =
                offerRepository.existsByApplicationIdAndStatus(
                        applicationId,
                        OfferStatus.ACCEPTED
                );

        if (acceptedOfferExists) {
            throw new ConflictException(
                    "Cannot create a new revision because an offer has already been accepted."
            );
        }

        // Mark previous pending offers as superseded
        List<Offer> pendingOffers =
                offerRepository.findByApplicationIdAndStatus(
                        applicationId,
                        OfferStatus.PENDING
                );

        for (Offer offer : pendingOffers) {
            offer.setStatus(OfferStatus.SUPERSEDED);

            activityService.recordSystemActivity(
                    application,
                    ActivityType.OFFER,
                    "Offer superseded",
                    "Offer revision " + offer.getRevisionNumber()
                    + " was superseded by a new revision."
            );
        }

        offerRepository.saveAll(pendingOffers);

        // Generate the next revision number using the highest existing revision
        int nextRevisionNumber = offerRepository
                .findFirstByApplicationIdOrderByRevisionNumberDesc(applicationId)
                .map(offer -> offer.getRevisionNumber() + 1)
                .orElse(1);

        Offer offer = new Offer(
                application,
                nextRevisionNumber,
                request.getOfferedCtc(),
                request.getFixedCtc(),
                request.getVariableCtc(),
                request.getJoiningBonus(),
                request.getOfferDate(),
                request.getValidityDate(),
                request.getNotes()
        );

        Offer savedOffer = offerRepository.save(offer);

        activityService.recordSystemActivity(
                application,
                ActivityType.OFFER,
                "New offer received",
                "Offer revision " + savedOffer.getRevisionNumber()
                + " received with offered CTC of "
                + savedOffer.getOfferedCtc() + " INR."
        );

        return mapToResponse(savedOffer);
    }

    // Get all offer revisions for a job application
    public List<OfferResponse> getOffersByApplication(Long applicationId) {

        if (!jobApplicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException(
                    "Job application not found with ID: " + applicationId
            );
        }

        return offerRepository
                .findByApplicationIdOrderByRevisionNumberAsc(applicationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get a specific offer
    public OfferResponse getOfferById(Long id) {

        Offer offer = getOfferEntity(id);

        return mapToResponse(offer);
    }

    // Update a pending offer
    @Transactional
    public OfferResponse updateOffer(Long id, OfferRequest request) {

        Offer offer = getOfferEntity(id);

        ensurePending(offer);

        validateOfferDates(request);

        offer.setOfferedCtc(request.getOfferedCtc());
        offer.setFixedCtc(request.getFixedCtc());
        offer.setVariableCtc(request.getVariableCtc());
        offer.setJoiningBonus(request.getJoiningBonus());
        offer.setOfferDate(request.getOfferDate());
        offer.setValidityDate(request.getValidityDate());
        offer.setNotes(request.getNotes());

        Offer updatedOffer = offerRepository.save(offer);

        activityService.recordSystemActivity(
                offer.getApplication(),
                ActivityType.OFFER,
                "Offer details updated",
                "Updated offer revision " + offer.getRevisionNumber()
                + ". Revised offered CTC: "
                + offer.getOfferedCtc() + " INR."
        );

        return mapToResponse(updatedOffer);
    }

    // Accept or reject a pending offer
    @Transactional
    public OfferResponse updateOfferStatus(Long id, OfferStatus newStatus) {

        Offer offer = getOfferEntity(id);

        ensurePending(offer);

        if (newStatus == null) {
            throw new ConflictException("Offer status is required.");
        }

        if (newStatus != OfferStatus.ACCEPTED
                && newStatus != OfferStatus.REJECTED) {

            throw new ConflictException(
                    "You can only change a pending offer to ACCEPTED or REJECTED."
            );
        }

        if (newStatus == OfferStatus.ACCEPTED) {

            boolean anotherAcceptedOfferExists =
                    offerRepository.existsByApplicationIdAndStatus(
                            offer.getApplication().getId(),
                            OfferStatus.ACCEPTED
                    );

            if (anotherAcceptedOfferExists) {
                throw new ConflictException(
                        "An offer has already been accepted for this application."
                );
            }
        }

        offer.setStatus(newStatus);

        Offer updatedOffer = offerRepository.save(offer);

        activityService.recordSystemActivity(
                offer.getApplication(),
                ActivityType.OFFER,
                "Offer " + newStatus.name().toLowerCase(),
                "Offer revision " + offer.getRevisionNumber()
                + " was " + newStatus.name().toLowerCase() + "."
        );

        return mapToResponse(updatedOffer);
    }

    // Delete an offer
    @Transactional
    public void deleteOffer(Long id) {

        Offer offer = getOfferEntity(id);

        if (offer.getStatus() == OfferStatus.ACCEPTED) {
            throw new ConflictException(
                    "An accepted offer cannot be deleted."
            );
        }

        activityService.recordSystemActivity(
                offer.getApplication(),
                ActivityType.OFFER,
                "Offer deleted",
                "Deleted offer revision " + offer.getRevisionNumber() + "."
        );

        offerRepository.delete(offer);
    }

    private Offer getOfferEntity(Long id) {

        return offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Offer not found with ID: " + id
                ));
    }

    private void ensurePending(Offer offer) {

        if (offer.getStatus() != OfferStatus.PENDING) {
            throw new ConflictException(
                    "Only pending offers can be updated. Current status: "
                            + offer.getStatus()
            );
        }
    }

    private void validateOfferDates(OfferRequest request) {

        LocalDate offerDate = request.getOfferDate();
        LocalDate validityDate = request.getValidityDate();

        if (offerDate == null) {
            throw new ConflictException("Offer date is required.");
        }

        if (validityDate != null && validityDate.isBefore(offerDate)) {
            throw new ConflictException(
                    "Offer validity date cannot be before the offer date."
            );
        }
    }

    private OfferResponse mapToResponse(Offer offer) {

        return new OfferResponse(
                offer.getId(),
                offer.getApplication().getId(),
                offer.getApplication().getCompany().getCompanyName(),
                offer.getApplication().getJobRole(),
                offer.getRevisionNumber(),
                offer.getOfferedCtc(),
                offer.getFixedCtc(),
                offer.getVariableCtc(),
                offer.getJoiningBonus(),
                offer.getOfferDate(),
                offer.getValidityDate(),
                offer.getStatus(),
                offer.getNotes()
        );
    }
}
