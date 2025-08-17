package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.WebhookCalendlyService;
import com.valentin.reservacion_citas.domain.service.WebhookPayPalService;
import com.valentin.reservacion_citas.domain.service.implementation.WebhookCalendlyServiceImpl;
import com.valentin.reservacion_citas.web.dto.request.WebhookPayPalPayloadReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.request.WebhookAppointmentCreatedReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

	/*@GetMapping("/calendly/webhook-subscription")
	public ResponseEntity<MessageResDto> getExistSubscription() throws IOException, InterruptedException {
		MessageResDto messageResDto = calendlyService.getOrCreateSubscription();

		return new ResponseEntity<>(messageResDto, messageResDto.getStatus() == 201 ? HttpStatus.CREATED : HttpStatus.OK );
	}*/

	@PostMapping("/calendly/invitee-created")
	public ResponseEntity<MessageResDto> createAppointment(@RequestBody WebhookAppointmentCreatedReqDto payload) {
		return new ResponseEntity<>(calendlyService.handleAppointmentCreated(payload), HttpStatus.OK);
	}

	@PostMapping("/paypal/payment-capture-completed")
	public void createOrderAndChangePaymentStatus(@RequestBody WebhookPayPalPayloadReqDto payload) {
		payPalService.handlePaymentCompleted(payload);
	}
}
