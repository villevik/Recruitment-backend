package com.backend.recruitmentapp.model;

import javax.persistence.*;

/**
 * Represents a type of skill that is listed on the Recruitment application website.
 */
@Entity
@Table(name = "competence")
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competence_id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}