package com.nazeeh.jobapplicationtracker.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nazeeh.jobapplicationtracker.entity.ApplicationStatus;
import com.nazeeh.jobapplicationtracker.entity.ApplicationType;
import com.nazeeh.jobapplicationtracker.entity.JobApplication;
import com.nazeeh.jobapplicationtracker.service.JobApplicationService;

@Controller
public class JobApplicationController {

	private final JobApplicationService jobApplicationService;

	public JobApplicationController(JobApplicationService jobApplicationService) {
		this.jobApplicationService = jobApplicationService;
	}

	@GetMapping("/applications")
	public String overview(@RequestParam(required = false) String query, @RequestParam(required = false) String status,Authentication authentication,
			Model model) {
		String email = authentication.getName();
		model.addAttribute("totalApplications", jobApplicationService.getTotalApplicationsForUser(authentication.getName()));
		model.addAttribute("notAppliedCount", jobApplicationService.getCountByStatusForUser(ApplicationStatus.NOT_APPLIED, authentication.getName()));
		model.addAttribute("appliedCount", jobApplicationService.getCountByStatusForUser(ApplicationStatus.APPLIED, authentication.getName()));
		model.addAttribute("inReviewCount", jobApplicationService.getCountByStatusForUser(ApplicationStatus.IN_REVIEW, authentication.getName()));
		model.addAttribute("interviewScheduledCount",
				jobApplicationService.getCountByStatusForUser(ApplicationStatus.INTERVIEW_SCHEDULED, authentication.getName()));
		model.addAttribute("rejectedCount", jobApplicationService.getCountByStatusForUser(ApplicationStatus.REJECTED, authentication.getName()));
		model.addAttribute("acceptedCount", jobApplicationService.getCountByStatusForUser(ApplicationStatus.ACCEPTED, authentication.getName()));

		model.addAttribute("selectedQuery", query);
		model.addAttribute("selectedStatus", status);
		model.addAttribute("statuses", ApplicationStatus.values());
		model.addAttribute("applications", jobApplicationService.getApplicationsForUser(email));

		return "index";
	}

	@GetMapping("/applications/new")
	public String showCreateForm(Model model) {
		JobApplication jobApplication = new JobApplication();
		jobApplication.setStatus(ApplicationStatus.APPLIED);
		model.addAttribute("jobApplication", jobApplication);
		model.addAttribute("applicationTypes", ApplicationType.values());

		return "application-form";
	}

	@PostMapping("/applications")
	public String createApplication(JobApplication jobApplication, Authentication authentication) {
		jobApplicationService.saveForUser(jobApplication, authentication.getName());
		return "redirect:/applications";
	}

	@GetMapping("/applications/edit/{id}")
	public String updateApplication(@PathVariable Long id,
	                                Authentication authentication,
	                                Model model) {

	    JobApplication jobApplication =
	        jobApplicationService.findByIdAndUserEmail(id, authentication.getName());

	    model.addAttribute("jobApp", jobApplication);
	    model.addAttribute("applicationStatuses", ApplicationStatus.values());

	    return "update-application";
	}

	@PostMapping("/applications/update/{id}")
	public String updateApplication(@PathVariable Long id,
			@ModelAttribute("jobApplication") JobApplication formApplication, Authentication authentication) {

		jobApplicationService.updateApplication(id, formApplication, authentication.getName());
		return "redirect:/applications";
	}

	@GetMapping("/applications/delete/confirm/{id}")
	public String showDeleteConfirmation(@PathVariable Long id,
	                                     Authentication authentication,
	                                     Model model) {

	    JobApplication jobApp =
	        jobApplicationService.findByIdAndUserEmail(id, authentication.getName());

	    model.addAttribute("jobApp", jobApp);
	    return "delete-confirmation";
	}

	@PostMapping("/applications/delete/{id}")
	public String deleteApplication(@PathVariable Long id,
	                               @RequestParam("positionConfirmation") String positionConfirmation,
	                               Authentication authentication,
	                               Model model) {

	    JobApplication jobApp =
	        jobApplicationService.findByIdAndUserEmail(id, authentication.getName());

	    if (!jobApp.getPosition().equals(positionConfirmation)) {
	        model.addAttribute("jobApp", jobApp);
	        model.addAttribute("error", "The entered position does not match.");
	        return "delete-confirmation";
	    }

	    jobApplicationService.deleteApplicationForUser(id, authentication.getName());
	    return "redirect:/applications";
	}
}