package com.valentin.reservacion_citas.domain.service;

import com.paypal.sdk.models.Order;

public interface OrderService {
	Order createOrder();

	Order captureOrder();
}
