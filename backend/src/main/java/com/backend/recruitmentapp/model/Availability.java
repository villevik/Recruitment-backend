package com.backend.recruitmentapp.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents the time frame that an applicant is available for duty.
 */
@Entity
@Table(name = "availability")
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "availability_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
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

    public Availability(Person person, LocalDate fromDate, LocalDate toDate) {
        this.person = person;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Availability()
    {

    }

}