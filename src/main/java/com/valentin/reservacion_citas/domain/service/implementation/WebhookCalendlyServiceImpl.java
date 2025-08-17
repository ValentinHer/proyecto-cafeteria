package com.valentin.reservacion_citas.domain.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentin.reservacion_citas.domain.service.AppointmentService;
import com.valentin.reservacion_citas.domain.service.NotificationService;
import com.valentin.reservacion_citas.domain.service.WebhookCalendlyService;
import com.valentin.reservacion_citas.persistence.entity.AppointmentStatus;
import com.valentin.reservacion_citas.web.dto.request.AppointmentReqDto;
import com.valentin.reservacion_citas.web.dto.request.GuestReqDto;
import com.valentin.reservacion_citas.web.dto.request.WebhookAppointmentCreatedReqDto;
import com.valentin.reservacion_citas.web.dto.request.WebhookCalendlySubscriptionReqDto;
import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import com.valentin.reservacion_citas.web.dto.response.WebhookCalendlyCreateSubscriptionResDto;
import com.valentin.reservacion_citas.web.dto.response.WebhookCalendlyListSubscriptionsResDto;
import com.valentin.reservacion_citas.web.dto.response.WebhookCalendlySubscriptionResDto;
import com.valentin.reservacion_citas.web.exception.ForbiddenException;
import com.valentin.reservacion_citas.web.exception.InternalServerException;
import com.valentin.reservacion_citas.web.exception.UnauthorizedException;
import jakarta.annotation.PostConstruct;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class WebhookCalendlyServiceImpl implements WebhookCalendlyService {
	private final AppointmentService appointmentService;
	private final RestClient restClient;
	private final NotificationService notificationService;
	private final Logger logger = LoggerFactory.getLogger(WebhookCalendlyServiceImpl.class);

	@Value("${calendly.api.url}")
	private String calendlyApiUrl;

	@Value("${calendly.token}")
	private String calendlyToken;

	@Value("${calendly.org.url}")
	private String calendlyOrgUrl;

	@Value("${calendly.user.url}")
	private String calendlyUserUrl;

	@Value("${calendly.scope}")
	private String calendlyScope;

	@Value("${calendly.url.webhook.invitee.created}")
	private String webhookUrlCallback;


	@Autowired
	public WebhookCalendlyServiceImpl(AppointmentService appointmentService, RestClient restClient, NotificationService notificationService) {
		this.appointmentService = appointmentService;
		this.notificationService = notificationService;
		this.restClient = restClient;
	}

	public WebhookCalendlyListSubscriptionsResDto getAllSubscription() {
		WebhookCalendlyListSubscriptionsResDto results = restClient.get()
						 .uri(String.format("%swebhook_subscriptions?organization=%s&user=%s&scope=%s", calendlyApiUrl, calendlyOrgUrl, calendlyUserUrl, calendlyScope))
						 .header("Authorization", "Bearer " + calendlyToken)
						 .header("Content-Type", "application/json")
						 .retrieve()
						 .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),(request, response) -> {
							 String errorBody;
							 try (InputStream bodyStream = response.getBody()) {
								 errorBody = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
							 } catch (IOException e) {
								 errorBody = "No se pudo leer el cuerpo del error: " + e.getMessage();
							 }

							 int statusCode = response.getStatusCode().value();

							 switch (statusCode) {
								 case 400:
									 logger.error("Solicitud inválida para obtener la lista de suscripciones, argumentos enviados en la solicitud inválidos: {}", errorBody);
									 throw new BadRequestException("Solicitud inválida");
								 case 401:
									 logger.error("Solicitud para obtener la lista de suscripciones Inautorizado: {}", errorBody);
									 throw new UnauthorizedException("Solicitud Inautorizado");
								 case 403:
									 logger.error("Permisos para obtener la lista de suscripciones denegados: {}", errorBody);
									 throw new ForbiddenException("Permisos denegados");
								 default:
									 logger.error("Ocurrió un error al procesar la solicitud para obtener la lista de suscripciones: {}", errorBody);
									 throw new InternalServerException("Ocurrió un error al procesar la solicitud para obtener la lista de suscripciones");
							 }
						 })
						 .body(WebhookCalendlyListSubscriptionsResDto.class);

		logger.info("Suscripciones al weebhook de calendly obtenidos de forma exitosa: {}", results);
		return results;
	}

	@PostConstruct
	public void getOrCreateSubscription() {
		WebhookCalendlyListSubscriptionsResDto subscriptions = getAllSubscription();
		List<WebhookCalendlySubscriptionResDto> existSubscription = subscriptions.getCollection()
																				 .stream()
																				 .filter(subscription -> subscription.getCallbackUrl().equals(webhookUrlCallback) && subscription.getState().equals("active"))
																				 .toList();

		if (existSubscription.isEmpty()) {
			WebhookCalendlySubscriptionReqDto body = new WebhookCalendlySubscriptionReqDto();
			body.setUrl(webhookUrlCallback);
			body.setEvents(List.of("invitee.created"));
			body.setOrganization(calendlyOrgUrl);
			body.setUser(calendlyUserUrl);
			body.setScope(calendlyScope);

			String bodyRequest;

			try {
				ObjectMapper objectMapper = new ObjectMapper();
				bodyRequest = objectMapper.writeValueAsString(body);
				logger.info("Datos enviados en la solicitud de suscripción: {}", bodyRequest);
			} catch (JsonProcessingException e) {
				logger.error("No se pudieron procesar los datos de la solicitud para la subscripción", e);
				throw new InternalServerException("No se pudo procesar la solicitud para la subscripción");
			}

			WebhookCalendlyCreateSubscriptionResDto subscriptionCreated = restClient.post()
																					.uri(calendlyApiUrl + "webhook_subscriptions")
																					.header("Authorization", "Bearer " + calendlyToken)
																					.header("Content-Type", "application/json")
																					.body(bodyRequest)
																					.retrieve()
																					.onStatus( status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> {
																						String errorBody;

																						try (InputStream bodyStream = response.getBody()) {
																							errorBody = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
																						} catch (IOException e) {
																							errorBody = "No se pudo leer el cuerpo del error: " + e.getMessage();
																						}

																						int statusCode = response.getStatusCode().value();

																						switch (statusCode) {
																							case 400:
																								logger.error("Solicitud inválida al crear la subcripción, argumentos enviados en la solicitud inválidos: {}", errorBody);
																								throw new BadRequestException("Solicitud inválida");
																							case 401:
																								logger.error("Solicitud para crear la subcripción Inautorizado: {}", errorBody);
																								throw new UnauthorizedException("Solicitud Inautorizado");
																							case 403:
																								logger.error("Permisos denegados para crear la subcripción: {}", errorBody);
																								throw new ForbiddenException("Permisos denegados");
																							default:
																								logger.error("Error al procesar la solicitud de creación de la subcripción: {}", errorBody);
																								throw new InternalServerException("Ocurrió un error al procesar la solicitud de creación de subcripción");
																						}
																					})
																					.body(WebhookCalendlyCreateSubscriptionResDto.class);

			logger.info("Suscripción al weebhook de calendly creada de forma exitosa: {}", subscriptionCreated);
			return;
		}

		logger.info("Suscripción existente: {}", existSubscription);
	}

	@Override
	public void handleAppointmentCreated(WebhookAppointmentCreatedReqDto payload) {
		AppointmentReqDto appointmentReqDto = new AppointmentReqDto();
		appointmentReqDto.setStartTime(payload.getPayload().getScheduledEvent().getStartTime());
		appointmentReqDto.setEndTime(payload.getPayload().getScheduledEvent().getEndTime());
		appointmentReqDto.setStatus(AppointmentStatus.valueOf(payload.getPayload().getScheduledEvent().getStatus().toUpperCase()));
		appointmentReqDto.setCreatedAt(payload.getPayload().getScheduledEvent().getCreatedAt());

		GuestReqDto guestReqDto = new GuestReqDto();
		guestReqDto.setName(payload.getPayload().getName());
		guestReqDto.setEmail(payload.getPayload().getEmail());
		guestReqDto.setAppointments(List.of(appointmentReqDto));

		MessageResDto messageResDto = appointmentService.createAppointment(guestReqDto);

		String[] listDateAndTime = payload.getPayload()
										  .getScheduledEvent()
										  .getStartTime()
										  .toString()
										  .split("T");
		String appointmentDate = listDateAndTime[0];
		String appointmentHour = listDateAndTime[1];

		notificationService.sendAppointmentConfirmationEmail(payload.getPayload().getName(),
														payload.getPayload().getEmail(),
														appointmentDate,
														appointmentHour);
	}

	//@PostConstruct
	private void getCurrentUser() {
		String payload = restClient.get()
				.uri(calendlyApiUrl + "users/me")
								   .header("Authorization", "Bearer " + calendlyToken)
								   .header("Content-Type", "application/json")
				.retrieve()
				.body(String.class);

		logger.info("Body al obtener el usuario actual: {}", payload);
	}
}
