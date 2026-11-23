package com.jobtrackr.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "hr_contacts")
public class HRContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotBlank(message = "HR contact name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Please provide a valid email address")
    private String email;

    private String phone;

    private String designation;

    protected HRContact() {
    }

    public HRContact(
            Company company,
            String name,
            String email,
            String phone,
            String designation) {
        this.company = company;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.designation = designation;
    }

    public Long getId() {
        return id;
    }

    public Company getCompany() {
        return company;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
