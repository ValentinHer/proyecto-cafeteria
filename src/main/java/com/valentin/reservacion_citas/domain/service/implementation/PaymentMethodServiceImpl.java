package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.PaymentMethodService;
import com.valentin.reservacion_citas.persistence.entity.PaymentMethod;
import com.valentin.reservacion_citas.persistence.entity.PaymentProviders;
import com.valentin.reservacion_citas.persistence.entity.PaymentTypes;
import com.valentin.reservacion_citas.persistence.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
	private final PaymentMethodRepository paymentMethodRepository;

	public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
		this.paymentMethodRepository = paymentMethodRepository;
	}

	@Override
	public PaymentMethod createOrRetrievePaymentMethod(PaymentProviders paymentProvider, String userId) {
		Optional<PaymentMethod> paymentMethodFound = paymentMethodRepository.findByProviderAndUserId(paymentProvider, userId);

		if (paymentMethodFound.isEmpty()) {
			PaymentMethod newPaymentMethod = new PaymentMethod();
			newPaymentMethod.setUserId(userId);
			newPaymentMethod.setProvider(paymentProvider);
			newPaymentMethod.setType(PaymentTypes.CARD);
			newPaymentMethod.setIsActive(true);

			return paymentMethodRepository.save(newPaymentMethod);
		}

		return paymentMethodFound.get();
	}
}
