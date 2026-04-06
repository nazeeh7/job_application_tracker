package com.nazeeh.jobapplicationtracker.repository;

import com.nazeeh.jobapplicationtracker.entity.ApplicationStatus;
import com.nazeeh.jobapplicationtracker.entity.JobApplication;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
	
	long countByStatus(ApplicationStatus status);
	
	List<JobApplication> findByCompanyContainingIgnoreCaseOrPositionContainingIgnoreCase(String company, String position);

    List<JobApplication> findByStatus(ApplicationStatus status);

    List<JobApplication> findByCompanyContainingIgnoreCaseAndStatus(String company, ApplicationStatus status);

    List<JobApplication> findByPositionContainingIgnoreCaseAndStatus(String position, ApplicationStatus status);
}