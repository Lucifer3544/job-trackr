package com.jobtrackr.controller;

import com.jobtrackr.dto.HRContactRequest;
import com.jobtrackr.dto.HRContactResponse;
import com.jobtrackr.service.HRContactService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HRContactController {

    private final HRContactService hrContactService;

    public HRContactController(HRContactService hrContactService) {
        this.hrContactService = hrContactService;
    }

    // Create HR contact
    @PostMapping("/api/companies/{companyId}/hr-contacts")
    @ResponseStatus(HttpStatus.CREATED)
    public HRContactResponse createHRContact(
            @PathVariable Long companyId,
            @Valid @RequestBody HRContactRequest request) {

        return hrContactService.createHRContact(companyId, request);
    }

    // Get HR contacts by company
    @GetMapping("/api/companies/{companyId}/hr-contacts")
    public List<HRContactResponse> getHRContactsByCompany(
            @PathVariable Long companyId) {

        return hrContactService.getHRContactsByCompany(companyId);
    }

    // Get HR contact by ID
    @GetMapping("/api/hr-contacts/{id}")
    public HRContactResponse getHRContactById(
            @PathVariable Long id) {

        return hrContactService.getHRContactById(id);
    }

    // Update HR contact
    @PutMapping("/api/hr-contacts/{id}")
    public HRContactResponse updateHRContact(
            @PathVariable Long id,
            @Valid @RequestBody HRContactRequest request) {

        return hrContactService.updateHRContact(id, request);
    }

    // Delete HR contact
    @DeleteMapping("/api/hr-contacts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHRContact(@PathVariable Long id) {

        hrContactService.deleteHRContact(id);
    }

    // Link HR contact to application
    @PostMapping(
        "/api/applications/{applicationId}/hr-contacts/{hrContactId}"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public void linkHRContactToApplication(
            @PathVariable Long applicationId,
            @PathVariable Long hrContactId) {

        hrContactService.linkHRContactToApplication(
                applicationId,
                hrContactId
        );
    }

    // Unlink HR contact from application
    @DeleteMapping(
        "/api/applications/{applicationId}/hr-contacts/{hrContactId}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkHRContactFromApplication(
            @PathVariable Long applicationId,
            @PathVariable Long hrContactId) {

        hrContactService.unlinkHRContactFromApplication(
                applicationId,
                hrContactId
        );
    }

    // Get HR contacts linked to an application
    @GetMapping("/api/applications/{applicationId}/hr-contacts")
    public List<HRContactResponse> getHRContactsByApplication(
            @PathVariable Long applicationId) {

        return hrContactService.getHRContactsByApplication(applicationId);
    }
}