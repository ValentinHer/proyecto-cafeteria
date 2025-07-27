package com.valentin.reservacion_citas.persistence.repository;

import com.valentin.reservacion_citas.persistence.entity.Product;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ListCrudRepository<Product, String> {
}
