package com.valentin.reservacion_citas.domain.service;

import com.paypal.sdk.models.Order;
import com.valentin.reservacion_citas.web.dto.request.OrderReqDto;

public interface OrderService {
	Order createOrder(String email);

	Order captureOrder(String orderId, String email);
}
