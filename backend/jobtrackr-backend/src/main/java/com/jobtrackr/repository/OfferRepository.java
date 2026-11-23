package com.jobtrackr.repository;

import com.jobtrackr.entity.Offer;
import com.jobtrackr.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByApplicationIdOrderByRevisionNumberAsc(Long applicationId);

    List<Offer> findByApplicationIdAndStatus(
            Long applicationId,
            OfferStatus status
    );

    Optional<Offer> findFirstByApplicationIdOrderByRevisionNumberDesc(
            Long applicationId
    );

    boolean existsByApplicationIdAndStatus(
            Long applicationId,
            OfferStatus status
    );
}