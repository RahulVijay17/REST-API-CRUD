package com.project.springboot.repository;

import com.project.springboot.entity.Roles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends CrudRepository<Roles,Long> {
    
    Optional<Roles> findByName (String name);
}
