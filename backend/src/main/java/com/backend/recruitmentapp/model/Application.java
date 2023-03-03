package com.backend.recruitmentapp.model;

import java.util.ArrayList;

/**
 * Represents a job application submitted by a user.
 */
public class Application {
    private ArrayList<String> competences;
    private ArrayList<Double> experiences;
    private String fromDate;
    private String toDate;

    public ArrayList<String> getCompetences() {
        return competences;
    }

    public void setCompetences(ArrayList<String> competences) {
        this.competences = competences;
    }

    public ArrayList<Double> getExperiences() {
        return experiences;
    }

    public void setExperiences(ArrayList<Double> experiences) {
        this.experiences = experiences;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}