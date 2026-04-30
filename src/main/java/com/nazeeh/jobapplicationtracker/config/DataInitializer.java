package com.nazeeh.jobapplicationtracker.config;

import com.nazeeh.jobapplicationtracker.entity.CompanyAddress;
import com.nazeeh.jobapplicationtracker.entity.Company;
import com.nazeeh.jobapplicationtracker.entity.JobApplication;
import com.nazeeh.jobapplicationtracker.entity.ApplicationStatus;
import com.nazeeh.jobapplicationtracker.entity.ApplicationType;
import com.nazeeh.jobapplicationtracker.repository.JobApplicationRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(JobApplicationRepository repository) {
        return args -> {

            if (repository.count() == 1) {

                // -------- Company 1 --------
                CompanyAddress companyAddress1 = createCompanyAddress(
                        "Musterstraße", "10", "44135", "Dortmund"
                );

                Company company1 = createCompany(
                        "ALVARA Digital Solutions", companyAddress1
                );

                JobApplication app1 = createApplication(
                        company1,
                        "Java Developer",
                        ApplicationStatus.APPLIED,
                        ApplicationType.EMAIL,
                        "Max Mustermann",
                        "max@example.com"
                );

                // -------- Company 2 --------
                CompanyAddress CompanyAddress2 = createCompanyAddress(
                        "Hauptstraße", "5", "50667", "Köln"
                );

                Company company2 = createCompany(
                        "REWE Group", CompanyAddress2
                );

                JobApplication app2 = createApplication(
                        company2,
                        "Backend Developer",
                        ApplicationStatus.IN_REVIEW,
                        ApplicationType.IN_PERSON,
                        "Anna Schmidt",
                        "anna@example.com"
                );

                // -------- gleiche Company, zweite Bewerbung --------
                JobApplication app3 = createApplication(
                        company1,
                        "Fullstack Developer",
                        ApplicationStatus.APPLIED,
                        ApplicationType.ONLINE,
                        "Max Mustermann",
                        "max@example.com"
                );

                repository.save(app1);
                repository.save(app2);
                repository.save(app3);
            }
        };
    }

    private CompanyAddress createCompanyAddress(String street, String number, String zip, String city) {
        CompanyAddress CompanyAddress = new CompanyAddress();
        CompanyAddress.setStreet(street);
        CompanyAddress.setHouseNumber(number);
        CompanyAddress.setZipCode(zip);
        CompanyAddress.setCity(city);
        return CompanyAddress;
    }

    private Company createCompany(String name, CompanyAddress CompanyAddress) {
        Company company = new Company();
        company.setName(name);
        company.setAddress(CompanyAddress);
        return company;
    }

    private JobApplication createApplication(
            Company company,
            String position,
            ApplicationStatus status,
            ApplicationType type,
            String contactPerson,
            String email) {

        JobApplication app = new JobApplication();
        app.setCompany(company);
        app.setPosition(position);
        app.setStatus(status);
        app.setApplicationType(type);
        app.setApplicationDate(LocalDate.now());
        app.setContactPerson(contactPerson);
        app.setEmail(email);
        app.setNotes("Initial test data");

        return app;
    }
}