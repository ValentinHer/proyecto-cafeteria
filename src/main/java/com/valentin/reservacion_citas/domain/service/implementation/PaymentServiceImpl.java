package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.PaymentService;
import com.valentin.reservacion_citas.persistence.entity.CurrencyTypes;
import com.valentin.reservacion_citas.persistence.entity.Payment;
import com.valentin.reservacion_citas.persistence.entity.PaymentStatus;
import com.valentin.reservacion_citas.persistence.repository.PaymentRepository;
import com.valentin.reservacion_citas.web.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {
	private final PaymentRepository paymentRepository;

	public PaymentServiceImpl(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	@Override
	public Payment createPayment(String paymentMethodId, String userId, String cartId, BigDecimal totalAmount, String providerOrderId) {
		Payment newPayment = new Payment();
		newPayment.setUserId(userId);
		newPayment.setCartId(cartId);
		newPayment.setProviderOrderId(providerOrderId);
		newPayment.setPaymentMethodId(paymentMethodId);
		newPayment.setStatus(PaymentStatus.PENDING);
		newPayment.setTotalAmount(totalAmount);
		newPayment.setCurrency(CurrencyTypes.MXN);

		return paymentRepository.save(newPayment);
	}

	@Override
	public Payment findByProviderOrderId(String providerOrderId) {
		Payment paymentFound = paymentRepository.findByProviderOrderId(providerOrderId).orElseThrow(() -> new NotFoundException("Intento de Pago no encontrado"));
		return paymentFound;
	}
}
