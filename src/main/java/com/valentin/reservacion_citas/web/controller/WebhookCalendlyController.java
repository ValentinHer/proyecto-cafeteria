package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.implementation.WebhookCalendlyServiceImpl;
import com.valentin.reservacion_citas.web.dto.MessageResDto;
import com.valentin.reservacion_citas.web.dto.WebhookEventCreatedReqDto;
import com.valentin.reservacion_citas.web.dto.WebhookPayloadReqDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/webhooks/calendars")
public class WebhookCalendlyController {
	private final WebhookCalendlyServiceImpl calendlyService;

	public WebhookCalendlyController(WebhookCalendlyServiceImpl calendlyService) {
		this.calendlyService = calendlyService;
	}

	@GetMapping("/webhook_subscription")
	public ResponseEntity<MessageResDto> getExistSubscription() throws IOException, InterruptedException {
		MessageResDto messageResDto = calendlyService.getOrCreateSubscription();

		return new ResponseEntity<>(messageResDto, messageResDto.getStatus() == 201 ? HttpStatus.CREATED : HttpStatus.OK );
	}

	@PostMapping("/invitee_created")
	public ResponseEntity<MessageResDto> createAppointment(@RequestBody WebhookEventCreatedReqDto payload) {
		return new ResponseEntity<>(calendlyService.handleEventCreated(payload), HttpStatus.OK);
	}
}
