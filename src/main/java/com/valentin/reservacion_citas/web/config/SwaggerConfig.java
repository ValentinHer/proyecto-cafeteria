package com.valentin.reservacion_citas.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI api() {
		return new OpenAPI()
				.info(new Info()
							  .title("Proyecto Cafetería")
							  .description("Proyecto que presenta una solución integral para los servicios que ofrece una cafetería.")
							  .version("1.0.0"));
	}
}
