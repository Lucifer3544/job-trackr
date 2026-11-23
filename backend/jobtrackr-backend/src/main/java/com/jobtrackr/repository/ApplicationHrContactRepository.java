package com.jobtrackr.repository;

import com.jobtrackr.entity.ApplicationHrContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationHrContactRepository
        extends JpaRepository<ApplicationHrContact, Long> {

    boolean existsByApplicationIdAndHrContactId(
            Long applicationId,
            Long hrContactId);

    Optional<ApplicationHrContact>
    findByApplicationIdAndHrContactId(
            Long applicationId,
            Long hrContactId);

    List<ApplicationHrContact> findByApplicationId(Long applicationId);

    long countByHrContactId(Long hrContactId);

    void deleteByApplicationId(Long applicationId);
}