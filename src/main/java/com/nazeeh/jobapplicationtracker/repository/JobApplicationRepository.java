package com.nazeeh.jobapplicationtracker.repository;

import com.nazeeh.jobapplicationtracker.entity.ApplicationStatus;
import com.nazeeh.jobapplicationtracker.entity.JobApplication;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
	
	long countByStatusAndUserEmail(ApplicationStatus status, String email);
	
	List<JobApplication> findByCompanyContainingIgnoreCaseOrPositionContainingIgnoreCase(String company, String position);

    List<JobApplication> findByStatus(ApplicationStatus status);

    List<JobApplication> findByCompanyContainingIgnoreCaseAndStatus(String company, ApplicationStatus status);

    List<JobApplication> findByPositionContainingIgnoreCaseAndStatus(String position, ApplicationStatus status);
    
    List<JobApplication> findByUserEmail(String email);
    
    Optional<JobApplication> findByIdAndUserEmail(Long id, String email);
    
    long countByUserEmail(String email);
}