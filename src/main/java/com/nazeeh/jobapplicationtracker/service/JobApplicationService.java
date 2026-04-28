package com.nazeeh.jobapplicationtracker.service;

import com.nazeeh.jobapplicationtracker.entity.ApplicationStatus;
import com.nazeeh.jobapplicationtracker.entity.JobApplication;
import com.nazeeh.jobapplicationtracker.entity.User;
import com.nazeeh.jobapplicationtracker.repository.JobApplicationRepository;
import com.nazeeh.jobapplicationtracker.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class JobApplicationService {

	private final JobApplicationRepository jobApplicationRepository;
	
	private final UserRepository userRepository;

	public JobApplicationService(JobApplicationRepository repository,UserRepository userRepository ) {
		this.jobApplicationRepository = repository;
		this.userRepository = userRepository;
	}

	public long getTotalApplicationsForUser(String email) {
		return jobApplicationRepository.countByUserEmail(email);
	}

	public long getCountByStatusForUser(ApplicationStatus status, String email) {
		return jobApplicationRepository.countByStatusAndUserEmail(status, email);
	}
	

	public JobApplication saveForUser(JobApplication jobApplication, String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    jobApplication.setUser(user);

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

    
    public JobApplication updateApplication(Long id, JobApplication formApplication, String email) {
        JobApplication existingApplication = jobApplicationRepository.findByIdAndUserEmail(id, email)
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

    public void deleteApplicationForUser(Long id, String email) {
        JobApplication app = findByIdAndUserEmail(id, email);
        jobApplicationRepository.delete(app);
    }

	public List<JobApplication> getApplicationsForUser(String email) {
		
		return jobApplicationRepository.findByUserEmail(email);
	}
	
	public JobApplication findByIdAndUserEmail(Long id, String email) {
	    return jobApplicationRepository.findByIdAndUserEmail(id, email)
	        .orElseThrow(() -> new RuntimeException("Not found"));
	}

}