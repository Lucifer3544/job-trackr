package com.jobtrackr.dto;

public class HRContactResponse {

    private Long id;
    private Long companyId;
    private String companyName;
    private String name;
    private String email;
    private String phone;
    private String designation;

    public HRContactResponse(
            Long id,
            Long companyId,
            String companyName,
            String name,
            String email,
            String phone,
            String designation) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.designation = designation;
    }

    public Long getId() {
        return id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDesignation() {
        return designation;
    }
}
