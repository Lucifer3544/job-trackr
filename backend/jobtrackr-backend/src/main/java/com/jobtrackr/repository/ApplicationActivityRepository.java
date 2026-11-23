package com.jobtrackr.repository;

import com.jobtrackr.entity.ApplicationActivity;
import com.jobtrackr.enums.ActivityType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationActivityRepository
        extends JpaRepository<ApplicationActivity, Long> {

    List<ApplicationActivity> findByApplicationIdOrderByActivityDateDesc(
            Long applicationId
    );

    List<ApplicationActivity> findByApplicationIdAndActivityTypeOrderByActivityDateDesc(
            Long applicationId,
            ActivityType activityType
    );

    long countByApplicationId(Long applicationId);

    List<ApplicationActivity> findTop10ByOrderByActivityDateDesc();
}