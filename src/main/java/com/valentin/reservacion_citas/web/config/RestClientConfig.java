package com.valentin.reservacion_citas.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
	@Value("${calendly.api.url}")
	private String calendlyApiUrl;

	@Bean
	public RestClient restClient(RestClient.Builder restClientBuilder) {
		return restClientBuilder.baseUrl(calendlyApiUrl).build();
	}
}
