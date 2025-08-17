package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.WebhookCalendlyService;
import com.valentin.reservacion_citas.domain.service.WebhookPayPalService;
import com.valentin.reservacion_citas.domain.service.implementation.WebhookCalendlyServiceImpl;
import com.valentin.reservacion_citas.web.dto.request.WebhookPayPalPayloadReqDto;
import com.valentin.reservacion_citas.web.dto.request.WebhookAppointmentCreatedReqDto;
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

	@PostMapping("/calendly/invitee-created")
	public void createAppointment(@RequestBody WebhookAppointmentCreatedReqDto payload) {
		calendlyService.handleAppointmentCreated(payload);
	}

	@PostMapping("/paypal/payment-capture-completed")
	public void createOrderAndChangePaymentStatus(@RequestBody WebhookPayPalPayloadReqDto payload) {
		payPalService.handlePaymentCompleted(payload);
	}
}
