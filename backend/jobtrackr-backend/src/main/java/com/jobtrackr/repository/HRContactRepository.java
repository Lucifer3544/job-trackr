package com.jobtrackr.repository;

import com.jobtrackr.entity.HRContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HRContactRepository
        extends JpaRepository<HRContact, Long> {

    List<HRContact> findByCompanyId(Long companyId);

    long countByCompanyId(Long companyId);
}