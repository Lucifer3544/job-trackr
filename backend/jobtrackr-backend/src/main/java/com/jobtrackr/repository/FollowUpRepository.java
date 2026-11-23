package com.jobtrackr.repository;

import com.jobtrackr.entity.FollowUp;
import com.jobtrackr.enums.FollowUpStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

    List<FollowUp> findByApplicationIdOrderByFollowUpDateAsc(
            Long applicationId);

    List<FollowUp> findByStatusOrderByFollowUpDateAsc(
            FollowUpStatus status);

    long countByApplicationId(Long applicationId);

    // Count follow-ups referencing a particular HR contact
    long countByHrContactId(Long hrContactId);

    List<FollowUp>
        findTop5ByFollowUpDateGreaterThanEqualAndStatusOrderByFollowUpDateAsc(
                LocalDate today,
                FollowUpStatus status);
}
