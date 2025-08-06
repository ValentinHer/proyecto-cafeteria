package com.valentin.reservacion_citas.web.controller;

import com.paypal.sdk.models.Order;
import com.valentin.reservacion_citas.domain.service.OrderService;
import com.valentin.reservacion_citas.web.dto.request.OrderReqDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Order> create(@RequestBody OrderReqDto orderReqDto) {
		return new ResponseEntity<>(orderService.createOrder(orderReqDto), HttpStatus.OK);
	}

	@PostMapping("/{id}/capture")
	public ResponseEntity<Order> captureOrder(@PathVariable("id") String orderId) {
		return new ResponseEntity<>(orderService.captureOrder(orderId), HttpStatus.OK);
	}
}
