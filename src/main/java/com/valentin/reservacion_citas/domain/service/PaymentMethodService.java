package com.valentin.reservacion_citas.domain.service;

import com.valentin.reservacion_citas.persistence.entity.PaymentMethod;
import com.valentin.reservacion_citas.persistence.entity.PaymentProviders;

public interface PaymentMethodService {
	PaymentMethod createOrRetrievePaymentMethod(PaymentProviders paymentProvider, String userId);
}
