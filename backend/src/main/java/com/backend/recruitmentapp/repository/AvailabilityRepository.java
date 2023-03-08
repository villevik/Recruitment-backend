package com.backend.recruitmentapp.repository;

import com.backend.recruitmentapp.model.Availability;
import com.backend.recruitmentapp.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * CRUD repository for the AvailabilityProfile entity
 */
public interface AvailabilityRepository extends CrudRepository<Availability, Integer> {

    /**
     * Finds all availabilities for a specific person.
     *
     * @param person An instance of Person.
     */
    List<Availability> findAvailabilitiesByPerson(Person person);

    /**
     * Finds all availabilities given the availability_id.
     *
     * @param id availability_id in the availability table.
     */
    List<Availability> findAvailabilitiesById(Integer id);

    /**
     * Finds all rows from the availability table.
     */
    @Query(value = "SELECT * FROM Availability",
            nativeQuery = true)
    List<Availability> findAllAvailabilityRows();

    /**
     * Finds all unique person ID's from the availability table.
     */
    @Query(value = "SELECT DISTINCT person_id FROM Availability",
            nativeQuery = true)
    List<Integer> findAllDistinctApplicants();

    /**
     * Finds all person ID's from the availability table.
     */
    @Query(value = "SELECT person_id FROM Availability",
            nativeQuery = true)
    List<Integer> findAllApplicants();

}
