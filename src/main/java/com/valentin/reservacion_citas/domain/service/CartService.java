package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.persistence.entity.Cart;
import com.valentin.reservacion_citas.web.dto.response.CartResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;

import java.math.BigDecimal;

public interface CartService {
	Cart createOrGetActiveCartByUser(String email);

	Cart getCartById(String cartId);

	MsgDataResDto<CartResDto> getCartWithItems(String email);

	MessageResDto changeCartStatus(String email);

	Cart getActiveCart(String email);

	void updateTotalAmount(Cart cart, BigDecimal totalAmount);
}
