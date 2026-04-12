package com.nazeeh.jobapplicationtracker.service;

import com.nazeeh.jobapplicationtracker.entity.ApplicationStatus;
import com.nazeeh.jobapplicationtracker.entity.JobApplication;
import com.nazeeh.jobapplicationtracker.repository.JobApplicationRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

	private final JobApplicationRepository jobApplicationRepository;

	public JobApplicationService(JobApplicationRepository repository) {
		this.jobApplicationRepository = repository;
	}

	public long getTotalApplications() {
		return jobApplicationRepository.count();
	}

	public long getCountByStatus(ApplicationStatus status) {
		return jobApplicationRepository.countByStatus(status);
	}
	
	public List<JobApplication> getAllApplications() {
	    return jobApplicationRepository.findAll();
	}
	
    public JobApplication save(JobApplication jobApplication) {
        return jobApplicationRepository.save(jobApplication);
    }
    
    public List<JobApplication> searchApplications(String query, String status) {
        boolean hasQuery = query != null && !query.trim().isEmpty();
        boolean hasStatus = status != null && !status.trim().isEmpty();

        if (!hasQuery && !hasStatus) {
            return jobApplicationRepository.findAll();
        }

        if (hasQuery && !hasStatus) {
            return jobApplicationRepository
                    .findByCompanyContainingIgnoreCaseOrPositionContainingIgnoreCase(query, query);
        }

        if (!hasQuery) {
            return jobApplicationRepository.findByStatus(ApplicationStatus.valueOf(status));
        }

        ApplicationStatus applicationStatus = ApplicationStatus.valueOf(status);

        List<JobApplication> byCompany =
                jobApplicationRepository.findByCompanyContainingIgnoreCaseAndStatus(query, applicationStatus);

        List<JobApplication> byPosition =
                jobApplicationRepository.findByPositionContainingIgnoreCaseAndStatus(query, applicationStatus);

        byPosition.stream()
                .filter(application -> !byCompany.contains(application))
                .forEach(byCompany::add);

        return byCompany;
    }

    public JobApplication findById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }
    
    public JobApplication updateApplication(Long id, JobApplication formApplication) {
        JobApplication existingApplication = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        existingApplication.getCompany().setName(
                getStringOrDefault(formApplication.getCompany().getName(), existingApplication.getCompany().getName()));
        existingApplication.setPosition(
                getStringOrDefault(formApplication.getPosition(), existingApplication.getPosition()));
        existingApplication.setApplicationType(
                getOrDefault(formApplication.getApplicationType(), existingApplication.getApplicationType()));
        existingApplication.setApplicationDate(
                getOrDefault(formApplication.getApplicationDate(), existingApplication.getApplicationDate()));
        existingApplication.setContactPerson(
                getStringOrDefault(formApplication.getContactPerson(), existingApplication.getContactPerson()));
        existingApplication.setEmail(
                getStringOrDefault(formApplication.getEmail(), existingApplication.getEmail()));
        existingApplication.setPhone(
                getStringOrDefault(formApplication.getPhone(), existingApplication.getPhone()));
        existingApplication.getCompany().getAddress().setStreet(
                getStringOrDefault(formApplication.getCompany().getAddress().getStreet(), existingApplication.getCompany().getAddress().getStreet()));
        existingApplication.setNotes(
                getStringOrDefault(formApplication.getNotes(), existingApplication.getNotes()));
        existingApplication.setStatus(
                getOrDefault(formApplication.getStatus(), existingApplication.getStatus()));
        

        return jobApplicationRepository.save(existingApplication);
    }

    private <T> T getOrDefault(T newValue, T oldValue) {
        return newValue != null ? newValue : oldValue;
    }

    private String getStringOrDefault(String newValue, String oldValue) {
        return (newValue != null && !newValue.trim().isEmpty()) ? newValue : oldValue;
    }
}