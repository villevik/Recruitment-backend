package com.backend.recruitmentapp.model;

import javax.persistence.*;

@Entity
@Table(name = "status")

public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id", nullable = false)
    private Integer id;
    @Column(name = "status")
    private String status;
    @Column(name = "person_id")
    private Integer person_id;

    public Status(String status, Integer person_id) {
        this.status = status;
        this.person_id = person_id;
    }

    public Status() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
