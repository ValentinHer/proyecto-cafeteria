package com.valentin.reservacion_citas.web.controller;

import com.paypal.sdk.models.Order;
import com.valentin.reservacion_citas.domain.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
public class OrderController {
	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Order> create(Principal principal) {
		return new ResponseEntity<>(orderService.createPayPalOrder(principal.getName()), HttpStatus.OK);
	}

	@PostMapping("/{id}/capture")
	public ResponseEntity<Order> captureOrder(@PathVariable("id") String orderId, Principal principal) {
		return new ResponseEntity<>(orderService.capturePayPalOrder(orderId), HttpStatus.OK);
	}
}
