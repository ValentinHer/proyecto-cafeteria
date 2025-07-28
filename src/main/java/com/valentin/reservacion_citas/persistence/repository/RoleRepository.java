package com.valentin.reservacion_citas.persistence.repository;

import com.valentin.reservacion_citas.persistence.entity.Role;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends ListCrudRepository<Role, String> {
	Optional<Role> findByName(String name);
}
