package com.valentin.reservacion_citas.web.controller;

import com.valentin.reservacion_citas.domain.service.implementation.WebhookCalendlyServiceImpl;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.request.WebhookAppointmentCreatedReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/webhooks/calendars")
public class WebhookCalendlyController {
	private final WebhookCalendlyServiceImpl calendlyService;

	@Autowired
	public WebhookCalendlyController(WebhookCalendlyServiceImpl calendlyService) {
		this.calendlyService = calendlyService;
	}

	/*@GetMapping("/webhook_subscription")
	public ResponseEntity<MessageResDto> getExistSubscription() throws IOException, InterruptedException {
		MessageResDto messageResDto = calendlyService.getOrCreateSubscription();

		return new ResponseEntity<>(messageResDto, messageResDto.getStatus() == 201 ? HttpStatus.CREATED : HttpStatus.OK );
	}*/

	@PostMapping("/invitee_created")
	public ResponseEntity<MessageResDto> createAppointment(@RequestBody WebhookAppointmentCreatedReqDto payload) {
		System.out.println(payload);
		return new ResponseEntity<>(calendlyService.handleAppointmentCreated(payload), HttpStatus.OK);
	}
}
