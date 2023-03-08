package com.backend.recruitmentapp.controller;

import com.backend.recruitmentapp.model.*;
import com.backend.recruitmentapp.repository.*;
import com.backend.recruitmentapp.security.UserDetailsImpl;
import com.backend.recruitmentapp.security.payload.request.StatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller that handles job applications.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    CompetenceProfileRepository competenceProfileRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CompetenceRepository competenceRepository;

    @Autowired
    AvailabilityRepository availabilityRepository;

    @Autowired
    StatusRepository statusRepository;


    /**
     * Gets a list of all applications.
     *
     * @return name, surname and status for each applicant.
     */
    @RequestMapping("/listApplications")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public String listApplications() {
        Iterable<Integer> list = availabilityRepository.findAllApplicants(); //ID of all applicants

        StringBuilder sb = new StringBuilder("{ \"Applications\" : [");
        int i = 0;
        for (Integer person_id : list) {
            if(i == 100) break; //limit to 100 applications (for testing
            String status = statusRepository.getStatus(person_id);
            if (status == null) {
                status = "unhandled";
            }
            sb.append("\"" + personRepository.findPersonById(person_id).getName() + "+"
                    + personRepository.findPersonById(person_id).getSurname() + "+" + status + "\",");
            i++
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        return sb.toString();
    }


    /**
     * Saves application or updates existing one.
     *
     * @param applicantData competences, experiences, fromDate, toDate
     */
    @RequestMapping("/save")
    public String saveApplicationTest(@Valid @RequestBody Application applicantData) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        //Object details = auth.getDetails();
        String userName = principal.getUsername();
        String role = authorities.stream().toList().get(0).toString();
        if (!role.equals("applicant")) {
            return "ERROR: You are not eligible to apply for a job!";
        }
        ArrayList<String> competences = applicantData.getCompetences();
        ArrayList<Double> experiences = applicantData.getExperiences();
        String fromDate = applicantData.getFromDate();
        String toDate = applicantData.getToDate();

        Person person = personRepository.findPersonByUsername(userName).get(0);

        for (int i = 0; i < competences.size(); i++) {
            String competenceName = competences.get(i);
            double experience = experiences.get(i);

            Competence competence = competenceRepository.findCompetenceByName(competenceName).get(0);
            BigDecimal yearsOfExperience = new BigDecimal(experience);

            // Check if row does not exist in the table
            List<CompetenceProfile> competenceProfileByPersonAndCompetence =
                    competenceProfileRepository.findCompetenceProfileByPersonAndCompetence(person, competence);
            if (competenceProfileByPersonAndCompetence.isEmpty()) {
                // Add to database table: competence_profile
                CompetenceProfile compProfile = new CompetenceProfile(person, competence, yearsOfExperience);
                competenceProfileRepository.save(compProfile);
            } else {
                // Update if competence profile already exists
                CompetenceProfile competenceProfile = competenceProfileByPersonAndCompetence.get(0);
                competenceProfile.setYearsOfExperience(yearsOfExperience);
                competenceProfileRepository.save(competenceProfile);
            }
        }
        LocalDate from = convertToLocalDate(fromDate);
        LocalDate to = convertToLocalDate(toDate);

        // Check if row does not exist in table
        List<Availability> availabilityList = availabilityRepository.findAvailabilitiesByPerson(person);
        if (availabilityList.isEmpty()) {
            // Add to database table: availability
            Availability availability = new Availability(person, from, to);
            availabilityRepository.save(availability);
            // Add to database table: status
            Status status = new Status("unhandled", person.getId());
            statusRepository.save(status);
        } else {
            // Modify row
            Availability availability = availabilityList.get(0);
            availability.setFromDate(from);
            availability.setToDate(to);
            availabilityRepository.save(availability);
        }

        return "OK!";
    }

    /**
     * Changes the status of an application.
     * @param statusParam status_id, new status
     */
    @RequestMapping("/changeStatus")
    public void changeStatus(@Valid @RequestBody StatusRequest statusParam) {

        Status newStatus = new Status();
        newStatus.setId(statusParam.getStatus_id());
        newStatus.setStatus(statusParam.getStatus());
        newStatus.setPerson_id(statusRepository.findPersonID(statusParam.getStatus_id()));
        statusRepository.save(newStatus);
    }

    private LocalDate convertToLocalDate(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.getDefault());
        return LocalDate.parse(inputDate, formatter);
    }

}
