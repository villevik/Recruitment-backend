package com.backend.recruitmentapp.repository;

import com.backend.recruitmentapp.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * CRUD repository for the Person entity
 */
public interface PersonRepository extends CrudRepository<Person, Integer> {

    /**
     * Finds a person by username.
     *
     * @param name Username
     */
    List<Person> findPersonByUsername(String name);

    Boolean existsByUsername(String username);

    /**
     * Checks if an email address is taken.
     *
     * @param email The email address that is searched for.
     * @return true if the email address is taken.
     */

    Boolean existsByEmail(String email);

    /**
     * Checks if a person number has been registered.
     *
     * @param pnr The person number that is searched for.
     * @return true if the person number has been registered.
     */
    Boolean existsByPnr(String pnr);

    List<Person> findPersonByEmail(String name);

    Person findPersonById(Integer id);


}