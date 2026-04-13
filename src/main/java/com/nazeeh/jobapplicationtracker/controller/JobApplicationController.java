package com.nazeeh.jobapplicationtracker.controller;

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

	@GetMapping("/")
	public String overview(@RequestParam(required = false) String query, @RequestParam(required = false) String status,
			Model model) {
		model.addAttribute("totalApplications", jobApplicationService.getTotalApplications());
		model.addAttribute("notAppliedCount", jobApplicationService.getCountByStatus(ApplicationStatus.NOT_APPLIED));
		model.addAttribute("appliedCount", jobApplicationService.getCountByStatus(ApplicationStatus.APPLIED));
		model.addAttribute("inReviewCount", jobApplicationService.getCountByStatus(ApplicationStatus.IN_REVIEW));
		model.addAttribute("interviewScheduledCount",
				jobApplicationService.getCountByStatus(ApplicationStatus.INTERVIEW_SCHEDULED));
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
		model.addAttribute("applicationTypes", ApplicationType.values());

		return "application-form";
	}

	@PostMapping("/applications")
	public String createApplication(JobApplication jobApplication) {
		jobApplicationService.save(jobApplication);
		return "redirect:/";
	}

	@GetMapping("/applications/edit/{id}")
	public String updateApplication(@PathVariable Long id, Model model) {
		JobApplication jobApplication = jobApplicationService.findById(id);
		model.addAttribute("jobApp", jobApplication);
		model.addAttribute("applicationStatuses", ApplicationStatus.values());
		return "update-application";
	}

	@PostMapping("/applications/update/{id}")
	public String updateApplication(@PathVariable Long id,
			@ModelAttribute("jobApplication") JobApplication formApplication) {

		jobApplicationService.updateApplication(id, formApplication);
		return "redirect:/";
	}

	@GetMapping("/applications/delete/confirm/{id}")
	public String showDeleteConfirmation(@PathVariable Long id, Model model) {
		JobApplication jobApp = jobApplicationService.findById(id);
		model.addAttribute("jobApp", jobApp);
		return "delete-confirmation";
	}

	@PostMapping("/applications/delete/{id}")
	public String deleteApplication(@PathVariable Long id,
			@RequestParam("positionConfirmation") String positionConfirmation, Model model) {

		JobApplication jobApp = jobApplicationService.findById(id);

		if (!jobApp.getPosition().equals(positionConfirmation)) {
			model.addAttribute("jobApp", jobApp);
			model.addAttribute("error", "The entered position does not match.");
			return "delete-confirmation";
		}
		jobApplicationService.deleteApplication(id);
		return "redirect:/";
	}
}