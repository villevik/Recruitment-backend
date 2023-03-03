package com.backend.recruitmentapp.repository;

import com.backend.recruitmentapp.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * CRUD repository for the Person entity
 */
public interface PersonRepository extends CrudRepository<Person, Integer> {

    Person findPersonById(Integer id);
    List<Person> findPersonByUsername(String name);
    List<Person> findPersonByEmail(String name);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Boolean existsByPnr(String pnr);

}