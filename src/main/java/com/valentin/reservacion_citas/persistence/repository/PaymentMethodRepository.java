package com.valentin.reservacion_citas.persistence.repository;

import com.valentin.reservacion_citas.persistence.entity.PaymentMethod;
import com.valentin.reservacion_citas.persistence.entity.PaymentProviders;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends ListCrudRepository<PaymentMethod, String> {
	Optional<PaymentMethod> findByProviderAndUserId(PaymentProviders paymentProvider, String userId);
}
