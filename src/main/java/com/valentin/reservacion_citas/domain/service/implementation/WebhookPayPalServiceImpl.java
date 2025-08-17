package com.valentin.reservacion_citas.domain.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentin.reservacion_citas.domain.service.CartService;
import com.valentin.reservacion_citas.domain.service.OrderService;
import com.valentin.reservacion_citas.domain.service.PaymentService;
import com.valentin.reservacion_citas.domain.service.WebhookPayPalService;
import com.valentin.reservacion_citas.persistence.entity.*;
import com.valentin.reservacion_citas.persistence.repository.CartRepository;
import com.valentin.reservacion_citas.persistence.repository.PaymentRepository;
import com.valentin.reservacion_citas.web.dto.request.WebhookPayPalCreateSubscriptionReqDto;
import com.valentin.reservacion_citas.web.dto.request.WebhookPayPalPayloadReqDto;
import com.valentin.reservacion_citas.web.dto.response.PayPalTokenResDto;
import com.valentin.reservacion_citas.web.dto.response.WebhookPayPalSubscriptionCreatedResDto;
import com.valentin.reservacion_citas.web.dto.response.WebhookPayPalSubscriptionListResDto;
import com.valentin.reservacion_citas.web.exception.InternalServerException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class WebhookPayPalServiceImpl implements WebhookPayPalService {
	private final CartRepository cartRepository;
	private final PaymentRepository paymentRepository;
	private final OrderService orderService;
	private final CartService cartService;
	private final PaymentService paymentService;
	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	private final Logger logger = LoggerFactory.getLogger(WebhookPayPalServiceImpl.class);

	@Value("${paypal.api.url}")
	private String paypalApiUrl;

	@Value("${paypal.url.webhook.payment.capture.completed}")
	private String webhookUrlPaymentCaptureCompleted;

	@Value("${paypal.client-id}")
	private String clientId;

	@Value("${paypal.client-secret}")
	private String clientSecret;

	private String paypalToken;

	public WebhookPayPalServiceImpl(CartRepository cartRepository, PaymentRepository paymentRepository, OrderService orderService, CartService cartService, PaymentService paymentService, RestClient restClient, ObjectMapper objectMapper) {
		this.cartRepository = cartRepository;
		this.paymentRepository = paymentRepository;
		this.orderService = orderService;
		this.cartService = cartService;
		this.paymentService = paymentService;
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	@PostConstruct
	private void initPayPalConfigWebhook() throws JsonProcessingException {
		getPaypalAccessToken();
		getOrCreateSubscription();
	}

	private void getPaypalAccessToken() {
		String credentials = String.format("%s:%s", clientId, clientSecret);
		String credentialsBase64 = Base64.getEncoder().encodeToString(credentials.getBytes());

		PayPalTokenResDto token = restClient.post()
				.uri(paypalApiUrl + "v1/oauth2/token")
				.header("Authorization", "Basic " + credentialsBase64)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.body("grant_type=client_credentials")
				.retrieve()
				.body(PayPalTokenResDto.class);

		logger.info("Datos obtenidos del token: {}", token);

		if (token !=  null) {
			paypalToken = token.getAccessToken();
		}
	}

	private WebhookPayPalSubscriptionListResDto getAllSubscriptions() {
		WebhookPayPalSubscriptionListResDto subscriptionListResDto = restClient.get()
				.uri(paypalApiUrl + "v1/notifications/webhooks")
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + paypalToken)
				.retrieve()
				.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> {
					String errorMsg;
					try(InputStream body = response.getBody()) {
						errorMsg = new String(body.readAllBytes(), StandardCharsets.UTF_8);
					} catch (IOException e) {
						errorMsg = "Error al obtener el mensaje " + e.getMessage();
					}

					int statusCode = response.getStatusCode().value();

					if (statusCode != 200) {
						logger.error("Error al obtener las subcripciones del webhook: {}", errorMsg);
						throw new InternalServerException("Ocurrió un error al obtener las susbcripciones del webhook");
					}
				})
				.body(WebhookPayPalSubscriptionListResDto.class);

		return subscriptionListResDto;
	}

	private void getOrCreateSubscription() throws JsonProcessingException {
		WebhookPayPalSubscriptionListResDto subscriptions = getAllSubscriptions();
		List<WebhookPayPalSubscriptionCreatedResDto> existsSubscription = subscriptions.getWebhooks().stream().filter(subscription -> subscription.getUrl().equals(webhookUrlPaymentCaptureCompleted)).toList();

		if (existsSubscription.isEmpty()) {
			WebhookPayPalCreateSubscriptionReqDto reqDto = new WebhookPayPalCreateSubscriptionReqDto(webhookUrlPaymentCaptureCompleted, List.of(Map.of("name", "PAYMENT.CAPTURE.COMPLETED")));
			String requestData = objectMapper.writeValueAsString(reqDto);

			WebhookPayPalSubscriptionCreatedResDto result = restClient.post()
																	  .uri(paypalApiUrl + "v1/notifications/webhooks")
																	  .header("Content-Type", "application/json")
																	  .header("Authorization", "Bearer " + paypalToken)
																	  .body(requestData)
																	  .retrieve()
																	  .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> {
																		  String errorBody;
																		  try (InputStream bodyStream = response.getBody()) {
																			  errorBody = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
																		  } catch (IOException e) {
																			  errorBody = "No se pudo leer el cuerpo del error: " + e.getMessage();
																		  }

																		  int statusCode = response.getStatusCode()
																								   .value();

																		  if (statusCode != 201) {
																			  logger.error("Error al procesar la solicitud de creación de la subcripción: {}", errorBody);
																			  throw new InternalServerException("Ocurrió un error al procesar la solicitud de creación de subcripción");
																		  }
																	  })
																	  .body(WebhookPayPalSubscriptionCreatedResDto.class);

			logger.info("Subcripción al webhook de PayPal creado {}", result);
			return;
		}

		logger.info("Subcripción del webhook de PayPal existente {}", existsSubscription);
	}

	@Override
	@Transactional
	public void handlePaymentCompleted(WebhookPayPalPayloadReqDto payload) {
		logger.info("Información de la payload del webhook de paypal al evento de PAYMENT.CAPTURE.COMPLETED: {}", payload);

		// Recuperar el intento de pago en base al orderId que genera PayPal
		Payment paymentFound = paymentService.findByProviderOrderId(payload.getResource().getSupplementaryData().getRelatedIds().getOrderId());
		paymentFound.setStatus(PaymentStatus.COMPLETED);

		Payment paymentUpdated = paymentRepository.save(paymentFound);
		logger.info("Intento de pago con id {} actualizado", paymentUpdated.getId());

		// Recuperar carrito del que se realizó la compra, cambiando el estatus de la misma a ORDERED
		Cart cartFound = cartService.getCartById(paymentFound.getCartId());
		cartFound.setStatus(CartStatus.ORDERED);

		Cart cartUpdated = cartRepository.save(cartFound);
		logger.info("Carrito con id {} actualizado", cartUpdated.getId());

		// Crear y vincular la orden con el carrito, el pago realizado y el usuario actual
		Order orderSaved = orderService.createOrder(paymentUpdated.getUserId(), cartUpdated.getId(), OrderStatus.CONFIRMED, cartUpdated.getTotalAmount(), payload.getResource().getId(), paymentUpdated);
		logger.info("Order con id {} creado", orderSaved.getId());
	}
}
