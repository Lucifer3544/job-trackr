
package com.jobtrackr.repository;

import com.jobtrackr.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByCompanyId(Long companyId);

    long countByCompanyId(Long companyId);
}
