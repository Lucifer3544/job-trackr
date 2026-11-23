
package com.jobtrackr.controller;

import com.jobtrackr.dto.CompanyRequest;
import com.jobtrackr.entity.Company;
import com.jobtrackr.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.jobtrackr.dto.CompanyUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompany(@Valid @RequestBody CompanyRequest request) {
        return companyService.createCompany(request.getCompanyName());
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id);
    }


    @PutMapping("/{id}")
    public Company updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyUpdateRequest request) {

        return companyService.updateCompany(
            id,
            request.getCompanyName()
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
    }
}
