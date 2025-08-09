package com.valentin.reservacion_citas.persistence.repository;

import com.valentin.reservacion_citas.persistence.entity.Category;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends ListCrudRepository<Category, String> {
	List<Category> findByIsActive(Boolean isActive);

	Optional<Category> findByName(String name);
}
