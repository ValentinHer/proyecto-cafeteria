package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.Order;
import com.valentin.reservacion_citas.persistence.entity.OrderItem;
import com.valentin.reservacion_citas.persistence.entity.OrderStatus;
import com.valentin.reservacion_citas.persistence.entity.Payment;
import com.valentin.reservacion_citas.web.dto.request.OrderReqDto;
import com.valentin.reservacion_citas.web.dto.response.OrderResDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
	private final OrderItemMapper orderItemMapper;

	public OrderMapper(OrderItemMapper orderItemMapper) {
		this.orderItemMapper = orderItemMapper;
	}

	public Order toEntity(OrderReqDto orderReqDto, Payment payment, String userId) {
		Order order = new Order();
		order.setUserId(userId);
		order.setPayment(payment);
		order.setStatus(OrderStatus.PAID);
		order.setTotalAmount(orderReqDto.getTotalPurchase());
		order.setProducts(orderReqDto.getItems().stream().map(item -> {
			OrderItem orderItem = orderItemMapper.toEntity(item);
			orderItem.setOrder(order);

			return orderItem;
		}).toList());

		return order;
	}

	public OrderResDto toResponse(Order order, Boolean withOrderItems) {
		OrderResDto response = new OrderResDto();
		response.setId(order.getId());
		response.setUserId(order.getUserId());
		response.setTotalAmount(order.getTotalAmount());
		response.setStatus(order.getStatus());
		response.setCreatedAt(order.getCreatedAt());

		if(withOrderItems) {
			response.setItems(order.getProducts().stream().map(product -> orderItemMapper.toResponse(product, false)).toList());
		}

		return response;
	}
}
