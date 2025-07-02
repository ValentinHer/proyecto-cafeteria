package com.valentin.reservacion_citas.domain.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentin.reservacion_citas.domain.service.WebhookCalendlyService;
import com.valentin.reservacion_citas.persistence.entity.Appointment;
import com.valentin.reservacion_citas.persistence.entity.AppointmentStatus;
import com.valentin.reservacion_citas.persistence.entity.User;
import com.valentin.reservacion_citas.persistence.repository.UserRepository;
import com.valentin.reservacion_citas.web.dto.*;
import com.valentin.reservacion_citas.web.exception.ForbiddenException;
import com.valentin.reservacion_citas.web.exception.InternalServerException;
import com.valentin.reservacion_citas.web.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class WebhookCalendlyServiceImpl implements WebhookCalendlyService {
	private final UserRepository userRepository;

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

	private final HttpClient httpClient = HttpClient.newHttpClient();
	private static final Logger logger = LoggerFactory.getLogger(WebhookCalendlyServiceImpl.class);

	public WebhookCalendlyServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public WebhookCalendlyListSubscriptionsResDto getAllSubscription() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(calendlyApiUrl + "webhook_subscriptions?" + String.format("organization=%s&user=%s&scope=%s", calendlyOrgUrl, calendlyUserUrl, calendlyScope)))
				.header("Authorization", "Bearer " + calendlyToken)
				.header("Content-Type", "application/json")
				.GET()
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if(response.statusCode() == 200) {
			ObjectMapper mapper = new ObjectMapper();

			WebhookCalendlyListSubscriptionsResDto results = mapper.readValue(response.body(), WebhookCalendlyListSubscriptionsResDto.class);
			logger.info("Suscripciones al weebhook de calendly obtenidos de forma exitosa: {}", results);
			return results;
		} else if (response.statusCode() == 400) {
			logger.error("Solicitud inválida para obtener la lista de suscripciones, argumentos enviados en la solicitud inválidos: {}", response.body());
			throw new BadRequestException("Solicitud inválida");
		} else if (response.statusCode() == 401) {
			logger.error("Solicitud para obtener la lista de suscripciones Inautorizado: {}", response.body());
			throw new UnauthorizedException("Solicitud Inautorizado");
		} else if (response.statusCode() == 403) {
			logger.error("Permisos para obtener la lista de suscripciones denegados: {}", response.body());
			throw new ForbiddenException("Permisos denegados");
		}

		throw new InternalServerException("Ocurrió un error al procesar la solicitud de obtener la lista de suscripciones");
	}

	@Override
	public MessageResDto getOrCreateSubscription() throws IOException, InterruptedException {
		WebhookCalendlyListSubscriptionsResDto subscriptions = getAllSubscription();
		List<WebhookCalendlySubscriptionResDto> existSubscription = subscriptions.getCollection().stream().filter(subscription -> subscription.getCallbackUrl().equals(webhookUrlCallback) && subscription.getState().equals("active")).toList();

		if (existSubscription.isEmpty()) {
			WebhookCalendlySubscriptionReqDto body = new WebhookCalendlySubscriptionReqDto();
			body.setUrl(webhookUrlCallback);
			body.setEvents(List.of("invitee.created"));
			body.setOrganization(calendlyOrgUrl);
			body.setUser(calendlyUserUrl);
			body.setScope(calendlyScope);

			ObjectMapper mapper = new ObjectMapper();
			String bodyRequest = mapper.writeValueAsString(body);
			logger.info("Body de la solicitud para crear subscripción: {}", bodyRequest);

			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(calendlyApiUrl + "webhook_subscriptions"))
					.header("Authorization", "Bearer " + calendlyToken)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(bodyRequest))
					.build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if(response.statusCode() == 201) {
				WebhookCalendlyCreateSubscriptionResDto subscriptionCreated = mapper.readValue(response.body(), WebhookCalendlyCreateSubscriptionResDto.class);

				logger.info("Suscripción al weebhook de calendly creada de forma exitosa: {}", subscriptionCreated);
				return new MessageResDto("Suscripción credada", HttpStatus.CREATED.value());
			} else if (response.statusCode() == 400) {
				logger.error("Solicitud inválida al crear la subcripción, argumentos enviados en la solicitud inválidos: {}", response.body());
				throw new BadRequestException("Solicitud inválida");
			} else if (response.statusCode() == 401) {
				logger.error("Solicitud para crear la subcripción Inautorizado: {}", response.body());
				throw new UnauthorizedException("Solicitud Inautorizado");
			} else if (response.statusCode() == 403) {
				logger.error("Permisos denegados para crear la subcripción: {}", response.body());
				throw new ForbiddenException("Permisos denegados");
			} else {
				logger.error("Error al procesar la solicitud de creación de la subcripción: {}", response.body());
				throw new InternalServerException("Ocurrió un error al procesar la solicitud de creación de subcripción");
			}

		}

		logger.info("Suscripción existente: {}", existSubscription);
		return new MessageResDto("La suscripción ya existe", HttpStatus.OK.value());
	}

	@Override
	@Transactional
	public MessageResDto handleEventCreated(WebhookEventCreatedReqDto payload) {
		Appointment appointment = new Appointment();
		appointment.setStartTime(payload.getPayload().getScheduledEvent().getStartTime());
		appointment.setEndTime(payload.getPayload().getScheduledEvent().getEndTime());
		appointment.setStatus(AppointmentStatus.valueOf(payload.getPayload().getScheduledEvent().getStatus().toUpperCase()));
		appointment.setCreatedAt(payload.getPayload().getScheduledEvent().getCreatedAt());

		User user = new User();
		user.setName(payload.getPayload().getName());
		user.setEmail(payload.getPayload().getEmail());
		appointment.setUser(user);
		user.setAppointments(List.of(appointment));

		User userCreated = userRepository.save(user);

		logger.info("Cita creada para el usuario {} de forma exitosa: {}", userCreated.getName(), userCreated.getAppointments());
		return new MessageResDto("Cita creada de forma exitosa", HttpStatus.OK.value());
	}
}
