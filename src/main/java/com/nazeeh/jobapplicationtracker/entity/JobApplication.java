package com.nazeeh.jobapplicationtracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_applications")
public class JobApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String company;

	@Column(nullable = false)
	private String position;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationStatus status = ApplicationStatus.APPLIED;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ApplicationType applicationType;

	@Column(nullable = false)
	private LocalDate applicationDate;

	private String contactPerson;

	private String email;

	private String phone;

	@Column(length = 2000)
	private String notes;

	@Column(nullable = false)
	private String address;
	// TODO
	//    private String street;
	//    private String city;
	//    private String zipCode;

	public JobApplication() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public LocalDate getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(LocalDate applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}