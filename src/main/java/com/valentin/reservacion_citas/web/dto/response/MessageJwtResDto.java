package com.valentin.reservacion_citas.web.dto.response;

public class MessageJwtResDto {
	private String message;
	private String token;
	private int status;

	public MessageJwtResDto() {
	}

	public MessageJwtResDto(String message, String token, int status) {
		this.message = message;
		this.token = token;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
