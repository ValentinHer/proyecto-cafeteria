package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.Cart;
import com.valentin.reservacion_citas.web.dto.response.CartResDto;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {
	private final CartItemMapper cartItemMapper;

	public CartMapper(CartItemMapper cartItemMapper) {
		this.cartItemMapper = cartItemMapper;
	}

	public CartResDto toResponse(Cart cart, Boolean withProductDetails) {
		CartResDto cartResDto = new CartResDto();
		cartResDto.setId(cart.getId());
		cartResDto.setStatus(cart.getStatus());
		cartResDto.setUserId(cart.getUserId());
		cartResDto.setTotalAmount(cart.getTotalAmount());
		cartResDto.setItems(cart.getProducts().stream().map(item -> cartItemMapper.toResponse(item, withProductDetails)).toList());
		cartResDto.setCreatedAt(cart.getCreatedAt());

		return cartResDto;
	}
}
