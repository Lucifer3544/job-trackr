package com.jobtrackr.service;

import com.jobtrackr.entity.Company;
import com.jobtrackr.exception.ConflictException;
import com.jobtrackr.exception.ResourceNotFoundException;
import com.jobtrackr.repository.CompanyRepository;
import com.jobtrackr.repository.JobApplicationRepository;
import com.jobtrackr.repository.HRContactRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final HRContactRepository hrContactRepository;

    public CompanyService(
            CompanyRepository companyRepository,
            JobApplicationRepository jobApplicationRepository,
            HRContactRepository hrContactRepository) {

        this.companyRepository = companyRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.hrContactRepository = hrContactRepository;
    }

    // Create company
    public Company createCompany(String companyName) {

        if (companyRepository.existsByCompanyName(companyName)) {
            throw new ConflictException(
                    "Company already exists: " + companyName
            );
        }

        Company company = new Company(companyName);

        return companyRepository.save(company);
    }

    // Get all companies
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // Get company by ID
    public Company getCompanyById(Long id) {

        return companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with ID: " + id
                        )
                );
    }

    // Update company
    @Transactional
    public Company updateCompany(Long id, String companyName) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with ID: " + id
                        )
                );

        companyRepository.findByCompanyName(companyName)
                .filter(existingCompany ->
                        !existingCompany.getId().equals(id)
                )
                .ifPresent(existingCompany -> {
                    throw new ConflictException(
                            "Company already exists: " + companyName
                    );
                });

        company.setCompanyName(companyName);

        return companyRepository.save(company);
    }

    // Delete company
    @Transactional
    public void deleteCompany(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with ID: " + id
                        )
                );

        // Check linked job applications
        long applicationCount =
                jobApplicationRepository.countByCompanyId(id);

        if (applicationCount > 0) {
            throw new ConflictException(
                    "Cannot delete company. " + applicationCount
                    + " job application(s) are linked to this company."
            );
        }

        // Check linked HR contacts
        long hrContactCount =
                hrContactRepository.countByCompanyId(id);

        if (hrContactCount > 0) {
            throw new ConflictException(
                    "Cannot delete company. " + hrContactCount
                    + " HR contact(s) are linked to this company."
            );
        }

        companyRepository.delete(company);
    }
}
