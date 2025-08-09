package com.valentin.reservacion_citas.persistence.repository;

import com.valentin.reservacion_citas.persistence.entity.Cart;
import com.valentin.reservacion_citas.persistence.entity.CartStatus;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends ListCrudRepository<Cart, String> {
	Optional<Cart> findByUserIdAndStatus(String userId, CartStatus status);
}
