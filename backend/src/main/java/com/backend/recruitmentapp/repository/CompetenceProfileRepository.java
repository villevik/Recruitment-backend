package com.backend.recruitmentapp.repository;

import com.backend.recruitmentapp.model.Competence;
import com.backend.recruitmentapp.model.CompetenceProfile;
import com.backend.recruitmentapp.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * CRUD repository for the CompetenceProfile entity
 */
public interface CompetenceProfileRepository extends CrudRepository<CompetenceProfile, Integer> {

    List<CompetenceProfile> findCompetenceProfileByPersonAndCompetence(Person person, Competence competence);

}
