package com.valentin.reservacion_citas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ReservacionCitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservacionCitasApplication.class, args);
	}

}
