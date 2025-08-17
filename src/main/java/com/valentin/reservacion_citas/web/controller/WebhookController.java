package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.WebhookCalendlyService;
import com.valentin.reservacion_citas.domain.service.WebhookPayPalService;
import com.valentin.reservacion_citas.domain.service.implementation.WebhookCalendlyServiceImpl;
import com.valentin.reservacion_citas.web.dto.request.WebhookPayPalPayloadReqDto;
import com.valentin.reservacion_citas.web.dto.request.WebhookAppointmentCreatedReqDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {
	private final WebhookCalendlyService calendlyService;
	private final WebhookPayPalService payPalService;

	@Autowired
	public WebhookController(WebhookCalendlyServiceImpl calendlyService, WebhookPayPalService payPalService) {
		this.calendlyService = calendlyService;
		this.payPalService = payPalService;
	}

	@Operation(summary = "Endpoint para manejar evento de Calendly", description = "Endpoint para manejar el evento invitee.created de Calendly")
	@PostMapping("/calendly/invitee-created")
	public void createAppointment(@RequestBody WebhookAppointmentCreatedReqDto payload) {
		calendlyService.handleAppointmentCreated(payload);
	}

	@Operation(summary = "Enpoint para manejar evento de PayPal", description = "Enpoint para manejar el evento de PAYMENT.CAPTURE.COMPLETED que manda PayPal")
	@PostMapping("/paypal/payment-capture-completed")
	public void createOrderAndChangePaymentStatus(@RequestBody WebhookPayPalPayloadReqDto payload) {
		payPalService.handlePaymentCompleted(payload);
	}
}
