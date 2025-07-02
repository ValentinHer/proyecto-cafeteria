package com.valentin.reservacion_citas.persistence.repository;

import com.valentin.reservacion_citas.persistence.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ListCrudRepository<User, String> {
}
