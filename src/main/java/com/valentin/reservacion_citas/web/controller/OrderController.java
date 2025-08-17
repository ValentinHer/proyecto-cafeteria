package com.valentin.reservacion_citas.web.controller;

import com.paypal.sdk.models.Order;
import com.valentin.reservacion_citas.domain.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@Operation(summary = "Crear orden de PayPal", description = "Crear una orden de PayPal")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Returna la orden creada desde PayPal"),
			@ApiResponse(responseCode = "500", description = "Error al crear la orden")
	})
	@PostMapping
	public ResponseEntity<Order> create(Principal principal) {
		return new ResponseEntity<>(orderService.createPayPalOrder(principal.getName()), HttpStatus.OK);
	}

	@Operation(summary = "Crear una captura de la orden en PayPal", description = "Crear una captura de la orden en PayPal")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Returna la Captura de la orden creada desde PayPal"),
			@ApiResponse(responseCode = "500", description = "Error al crear la captura de la orden")
	})
	@PostMapping("/{id}/capture")
	public ResponseEntity<Order> captureOrder(@PathVariable("id") String orderId, Principal principal) {
		return new ResponseEntity<>(orderService.capturePayPalOrder(orderId), HttpStatus.OK);
	}
}
