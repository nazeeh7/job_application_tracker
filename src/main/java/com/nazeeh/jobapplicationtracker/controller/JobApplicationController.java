package com.nazeeh.jobapplicationtracker.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nazeeh.jobapplicationtracker.entity.ApplicationStatus;
import com.nazeeh.jobapplicationtracker.entity.JobApplication;
import com.nazeeh.jobapplicationtracker.service.JobApplicationService;


@Controller
public class JobApplicationController {
	
	
	private final JobApplicationService jobApplicationService;
	
    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping("/")
    public String overview(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            Model model) {
        model.addAttribute("totalApplications", jobApplicationService.getTotalApplications());
        model.addAttribute("notAppliedCount", jobApplicationService.getCountByStatus(ApplicationStatus.NOT_APPLIED));
        model.addAttribute("appliedCount", jobApplicationService.getCountByStatus(ApplicationStatus.APPLIED));
        model.addAttribute("inReviewCount", jobApplicationService.getCountByStatus(ApplicationStatus.IN_REVIEW));
        model.addAttribute("interviewScheduledCount", jobApplicationService.getCountByStatus(ApplicationStatus.INTERVIEW_SCHEDULED));
        model.addAttribute("rejectedCount", jobApplicationService.getCountByStatus(ApplicationStatus.REJECTED));
        model.addAttribute("acceptedCount", jobApplicationService.getCountByStatus(ApplicationStatus.ACCEPTED));
        model.addAttribute("applications", jobApplicationService.getAllApplications());
        
        model.addAttribute("selectedQuery", query);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", ApplicationStatus.values());

        return "index";
    }
    
    @GetMapping("/applications/new")
    public String showCreateForm(Model model) {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setStatus(ApplicationStatus.APPLIED);
        model.addAttribute("jobApplication", jobApplication);
        model.addAttribute("statuses", ApplicationStatus.values());

        return "application-form";
    }
    
    @PostMapping("/applications")
    public String createApplication(JobApplication jobApplication) {
        jobApplicationService.save(jobApplication);
        return "redirect:/";
    }

}