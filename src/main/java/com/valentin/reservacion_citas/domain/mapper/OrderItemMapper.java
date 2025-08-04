package com.valentin.reservacion_citas.domain.mapper;

import com.valentin.reservacion_citas.persistence.entity.OrderItem;
import com.valentin.reservacion_citas.web.dto.request.OrderItemReqDto;
import com.valentin.reservacion_citas.web.dto.response.OrderItemResDto;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
	public OrderItem toEntity(OrderItemReqDto orderItemReqDto) {
		OrderItem orderItem = new OrderItem();
		orderItem.setProductId(orderItemReqDto.getProductId());
		orderItem.setQuantity(orderItemReqDto.getQuantity());
		orderItem.setUnitaryPrice(orderItemReqDto.getUnitaryPrice());
		orderItem.setTotalAmount(orderItemReqDto.getTotalAmount());

		return  orderItem;
	}

	public OrderItemResDto toResponse(OrderItem orderItem) {
		OrderItemResDto response = new OrderItemResDto();
		response.setId(orderItem.getId());
		response.setProductId(orderItem.getProductId());
		response.setQuantity(orderItem.getQuantity());
		response.setUnitaryPrice(orderItem.getUnitaryPrice());
		response.setTotalAmount(orderItem.getTotalAmount());

		return response;
	}
}
