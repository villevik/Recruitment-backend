package com.backend.recruitmentapp.repository;

import com.backend.recruitmentapp.model.Competence;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * CRUD repository for the Competence entity
 */
public interface CompetenceRepository extends CrudRepository<Competence, Integer> {

    List<Competence> findCompetenceByName(String name);

}
