package com.valentin.reservacion_citas.web.dto.request;

import com.valentin.reservacion_citas.persistence.entity.AuthProviders;

public class AuthProviderReqDto {
	private AuthProviders name;
	private String userId;
	private String providerUserId;
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

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
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
