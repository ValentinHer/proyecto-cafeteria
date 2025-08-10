package com.valentin.reservacion_citas.persistence.repository;

import com.valentin.reservacion_citas.persistence.entity.CartItem;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends ListCrudRepository<CartItem, String> {
	Optional<CartItem> findByProductIdAndCartId(String productId, String cartId);
}
