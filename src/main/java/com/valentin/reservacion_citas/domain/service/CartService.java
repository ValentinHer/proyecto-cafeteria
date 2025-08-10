package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.persistence.entity.Cart;
import com.valentin.reservacion_citas.web.dto.response.CartResDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.MsgDataResDto;

public interface CartService {
	Cart createOrRetrieveActiveCartByUser(String email);

	MsgDataResDto<CartResDto> getCartWithItems(String email);

	MessageResDto changeCartStatus(String email);
}
