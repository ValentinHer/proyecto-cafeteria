package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.*;
import com.valentin.reservacion_citas.web.dto.response.OrderResDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMapper {

	public Order toEntity(Payment payment, String userId, BigDecimal totalAmount) {
		Order order = new Order();
		order.setUserId(userId);
		order.setPayment(payment);
		order.setStatus(OrderStatus.CREATED);
		order.setTotalAmount(totalAmount);

		return order;
	}

	public OrderResDto toResponse(Order order, Boolean withOrderItems) {
		OrderResDto response = new OrderResDto();
		response.setId(order.getId());
		response.setUserId(order.getUserId());
		response.setTotalAmount(order.getTotalAmount());
		response.setStatus(order.getStatus());
		response.setCreatedAt(order.getCreatedAt());

		return response;
	}

	public Order toEntityFromCart(Cart cart, String userId) {
		Order order = new Order();
		order.setUserId(userId);
		order.setStatus(OrderStatus.CREATED);
		order.setTotalAmount(cart.getTotalAmount());

		return order;
	}
}
