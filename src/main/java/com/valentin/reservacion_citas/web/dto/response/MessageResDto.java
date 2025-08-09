package com.valentin.reservacion_citas.web.dto.response;

public class MessageResDto {
	private String message;
	private int status;

	public MessageResDto() {
	}

	public MessageResDto(String message, int status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MessageResDto{" +
				"message='" + message + '\'' +
				", status=" + status +
				'}';
	}
}
