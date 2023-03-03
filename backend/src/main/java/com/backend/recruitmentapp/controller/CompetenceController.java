package com.backend.recruitmentapp.controller;

import com.backend.recruitmentapp.model.Competence;
import com.backend.recruitmentapp.repository.CompetenceRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Controller that handles competences which are listed on the website.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/competence")
public class CompetenceController {


    @Autowired
    CompetenceRepository competenceRepository;

    /**
     * Shows all competences
     */
    @RequestMapping("/list")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")

    public String listCompetences() {
        Iterable<Competence> list = competenceRepository.findAll();
        ArrayList<String> compList = new ArrayList<>();
        for (Competence competence: list){

            compList.add(competence.getName());

        }

        System.out.println(new JSONArray(compList));
        return new JSONArray(compList).toString();

    }

}