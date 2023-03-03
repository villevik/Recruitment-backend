package com.backend.recruitmentapp.repository;

import com.backend.recruitmentapp.model.Person;
import com.backend.recruitmentapp.model.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * CRUD repository for the Status entity
 */
public interface StatusRepository extends CrudRepository<Status, Integer> {
    /**
     * Finds the status of an application.
     * @param id The person_id from the status table
     */
   @Query(value = "SELECT status FROM status u WHERE person_id = :id",
            nativeQuery = true)
    String getStatus(
            @Param("id") Integer id);

    /**
     * Gets person_id from the status table.
     * @param id status_id.
     */
    @Query(value = "SELECT person_id FROM status u WHERE status_id = :id",
            nativeQuery = true)
    Integer findPersonID(
            @Param("id") Integer id);

}
