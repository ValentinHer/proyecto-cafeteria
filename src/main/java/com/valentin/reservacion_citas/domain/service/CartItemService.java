package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.web.dto.request.CartItemReqDto;
import com.valentin.reservacion_citas.web.dto.response.CartItemResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;

public interface CartItemService {
	MsgDataResDto<CartItemResDto> addToCart(CartItemReqDto cartItemReqDto, String email);

	MessageResDto deleteItemFromCart(String cartItemId, String email);
}
