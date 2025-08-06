package com.valentin.reservacion_citas.web.config;

import com.paypal.sdk.Environment;
import com.paypal.sdk.PaypalServerSdkClient;
import com.paypal.sdk.authentication.ClientCredentialsAuthModel;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalClientConfig {

	private final String clientId;

	private final String clientSecret;

	public PaypalClientConfig(@Value("${paypal.client-id}") String clientId, @Value("${paypal.client-secret}") String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@Bean
	public PaypalServerSdkClient paypalServerSdkClient() {
		return new PaypalServerSdkClient.Builder()
				.loggingConfig(builder -> builder
						.level(Level.DEBUG)
						.requestConfig(logConfigBuilder -> logConfigBuilder.body(true))
						.responseConfig(logConfigBuilder -> logConfigBuilder.headers(true)))
				.httpClientConfig(configBuilder -> configBuilder.timeout(0))
				.clientCredentialsAuth(new ClientCredentialsAuthModel.Builder(
						clientId,
						clientSecret
				).build())
				.environment(Environment.SANDBOX)
				.build();
	}
}
