package com.valentin.reservacion_citas.web.dto.request;

import com.valentin.reservacion_citas.persistence.entity.AuthProviders;

public class AuthProviderReqDto {
	private AuthProviders name;
	private String userId;
	private String providerId;
	private String email;
	private String password;
	private String resetToken;

	public AuthProviders getName() {
		return name;
	}

	public void setName(AuthProviders name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
}
