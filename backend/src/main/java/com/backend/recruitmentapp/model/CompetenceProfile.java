package com.backend.recruitmentapp.model;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "competence_profile")
public class CompetenceProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competence_profile_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competence_id")
    private Competence competence;

    @Column(name = "years_of_experience", precision = 4, scale = 2)
    private BigDecimal yearsOfExperience;

    public BigDecimal getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(BigDecimal yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Competence getCompetence() {
        return competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CompetenceProfile(Person person, Competence competence, BigDecimal yearsOfExperience) {
        this.person = person;
        this.competence = competence;
        this.yearsOfExperience = yearsOfExperience;
    }

    public CompetenceProfile() {

    }

}