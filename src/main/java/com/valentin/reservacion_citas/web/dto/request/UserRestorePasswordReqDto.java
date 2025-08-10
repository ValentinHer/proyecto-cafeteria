package com.valentin.reservacion_citas.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserRestorePasswordReqDto {

	@NotNull
	@Email
	private String email;

	public @NotNull @Email String getEmail() {
		return email;
	}

	public void setEmail(@NotNull @Email String email) {
		this.email = email;
	}
}
