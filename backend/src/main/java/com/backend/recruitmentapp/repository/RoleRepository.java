package com.backend.recruitmentapp.repository;

import com.backend.recruitmentapp.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * CRUD repository for the Role entity
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
