package com.valentin.reservacion_citas.domain.service;

import com.paypal.sdk.models.Order;
import com.valentin.reservacion_citas.persistence.entity.OrderStatus;
import com.valentin.reservacion_citas.persistence.entity.Payment;

import java.math.BigDecimal;

public interface OrderService {
	com.valentin.reservacion_citas.persistence.entity.Order createOrder(String userId, String cartId, OrderStatus status, BigDecimal totalAmount, String providerPaymentId, Payment payment);

	Order createPayPalOrder(String email);

	Order capturePayPalOrder(String orderId);
}
