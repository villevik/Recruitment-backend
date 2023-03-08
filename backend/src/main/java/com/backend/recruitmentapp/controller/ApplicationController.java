package com.backend.recruitmentapp.controller;

import com.backend.recruitmentapp.model.*;
import com.backend.recruitmentapp.repository.*;
import com.backend.recruitmentapp.security.UserDetailsImpl;
import com.backend.recruitmentapp.security.payload.request.AvailabilityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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


    /**
     * Gets a list of all applications.
     *
     * @return name, surname and status for each applicant.
     */
    @RequestMapping("/listApplications")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public String listApplications(Authentication auth) {

        String role = null;
        try {
            role = authenticateRole(auth);
        } catch (Exception e) {
            return e.getMessage();
        }

        if (!role.equals("recruiter")) {
            return "ERROR: You are not eligible to access this information!";
        }

        Iterable<Availability> list = availabilityRepository.findAllAvailabilityRows();

        StringBuilder sb = new StringBuilder("{ \"Applications\" : [");
        int i = 0;
        for (Availability availability : list) {
            if (i == 100) break; //limit to 100 applications (for testing)
            i++;
            if (availability.getStatus() == null) {
                availability.setStatus("unhandled");
            }
            if (availability.getPerson() == null) {
                continue;
            }
            sb.append("\"" + availability.getPerson().getName() + "+" + availability.getPerson().getSurname() + "+" +
                    availability.getStatus() + /*"+" + availability.getId() +*/ "\",");

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
    @Transactional
    @RequestMapping("/save")
    public String saveApplication(Authentication auth, @Valid @RequestBody Application applicantData) {

        String role = null;
        try {
            role = authenticateRole(auth);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (!role.equals("applicant")) {
            return "ERROR: You are not eligible to apply for a job!";
        }
        String userName = null;
        try {
            userName = authenticateUser(auth);
        } catch (Exception e) {
            return e.getMessage();
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
            Availability availability = new Availability(person, from, to, "unhandled");
            availabilityRepository.save(availability);
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
     *
     * @param param AvailabilityRequest
     */
    @Transactional
    @RequestMapping("/changeStatus")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public String changeStatus(@Valid @RequestBody AvailabilityRequest param) {

        Integer id = param.getAvailability_id();
        List<Availability> availabilitiesById = availabilityRepository.findAvailabilitiesById(id);
        if (availabilitiesById.isEmpty()) {
            return "ERROR: No such availability_id";
        }

        if (availabilitiesById.size() > 1) {
            return "ERROR: ID not unique";
        }

        Availability availability = availabilitiesById.get(0);
        String status = param.getStatus();
        availability.setStatus(status);
        availabilityRepository.save(availability);
        return "OK";

    }

    /**
     * Resets each status column.
     * Sets each application status to "unhandled".
     */
    @Transactional
    @RequestMapping("/resetStatuses")
    public String resetStatuses(Authentication auth) {

        String role = null;
        try {
            role = authenticateRole(auth);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (!role.equals("recruiter")) {
            return "ERROR: You are not eligible to access this function!";
        }
        Iterable<Availability> availabilities = availabilityRepository.findAllAvailabilityRows();

        for (Availability application : availabilities) {

            application.setStatus("unhandled");
            availabilityRepository.save(application);
        }
        return "OK!";
    }

    private LocalDate convertToLocalDate(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.getDefault());
        return LocalDate.parse(inputDate, formatter);
    }

    private String authenticateRole(Authentication auth) throws Exception {
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String role = authorities.stream().toList().get(0).toString();*/
        if (auth == null)
        {
            throw new Exception ("ERROR: Access denied!");
        }
        //String role = user.getAuthorities().stream().toList().get(0).toString();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String role = authorities.stream().toList().get(0).toString();
        return role;
    }

    private String authenticateUser(Authentication auth) throws Exception {
        /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
        String userName = principal.getUsername();*/
        if (auth == null)
        {
            throw new Exception ("ERROR: Access denied!");
        }
        //String userName = auth.getUsername();
        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
        String userName = principal.getUsername();
        return userName;
    }
}
