package com.jobtrackr.repository;

import com.jobtrackr.entity.InterviewRound;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jobtrackr.enums.InterviewStatus;
import java.time.LocalDateTime;
import java.util.List;

import java.util.List;

public interface InterviewRoundRepository
        extends JpaRepository<InterviewRound, Long> {

    List<InterviewRound> findByApplicationIdOrderByRoundNumberAsc(
            Long applicationId);

    boolean existsByApplicationIdAndRoundNumber(
            Long applicationId,
            Integer roundNumber);

    boolean existsByApplicationIdAndRoundNumberAndIdNot(
            Long applicationId,
            Integer roundNumber,
            Long id);

    long countByApplicationId(Long applicationId);

    List<InterviewRound>
        findTop5ByScheduledAtGreaterThanEqualAndStatusInOrderByScheduledAtAsc(
            LocalDateTime now,
            List<InterviewStatus> statuses);
}
