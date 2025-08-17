package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.CartItem;
import com.valentin.reservacion_citas.web.dto.request.CartItemReqDto;
import com.valentin.reservacion_citas.web.dto.response.CartItemResDto;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
	private final ProductMapper productMapper;

	public CartItemMapper(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	public CartItem toEntity(CartItemReqDto cartItemReqDto, String cartId) {
		CartItem cartItem = new CartItem();
		cartItem.setProductId(cartItemReqDto.getProductId());
		cartItem.setQuantity(cartItemReqDto.getQuantity());
		cartItem.setUnitaryPrice(cartItemReqDto.getUnitaryPrice());
		cartItem.setTotalAmount(cartItemReqDto.getTotalAmount());
		cartItem.setCartId(cartId);

		return cartItem;
	}

	public CartItemResDto toResponse(CartItem cartItem, Boolean withProductDetails) {
		CartItemResDto response = new CartItemResDto();
		response.setId(cartItem.getId());
		response.setProductId(cartItem.getProductId());
		response.setQuantity(cartItem.getQuantity());
		response.setUnitaryPrice(cartItem.getUnitaryPrice());
		response.setTotalAmount(cartItem.getTotalAmount());

		if (withProductDetails) {
			response.setProductResDto(productMapper.toResponse(cartItem.getProduct(), false, false));
		}

		return response;
	}
}
