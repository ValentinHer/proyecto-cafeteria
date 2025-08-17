package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.persistence.entity.Payment;

import java.math.BigDecimal;

public interface PaymentService {
	Payment createPayment(String paymentMethodId, String userId, String cartId, BigDecimal TotalAmount, String providerOrderId);

	Payment findByProviderOrderId(String providerOrderId);
}
