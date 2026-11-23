package com.jobtrackr.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "application_hr_contacts",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_application_hr_contact",
            columnNames = {"application_id", "hr_contact_id"}
        )
    }
)
public class ApplicationHrContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_id", nullable = false)
    private JobApplication application;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hr_contact_id", nullable = false)
    private HRContact hrContact;

    protected ApplicationHrContact() {
    }

    public ApplicationHrContact(
            JobApplication application,
            HRContact hrContact) {
        this.application = application;
        this.hrContact = hrContact;
    }

    public Long getId() {
        return id;
    }

    public JobApplication getApplication() {
        return application;
    }

    public HRContact getHrContact() {
        return hrContact;
    }
}
