package com.valentin.reservacion_citas.web.dto.response;

public class MsgErrorResDto <T> {
	private MsgStatus status;

	private T message;

	public MsgStatus getStatus() {
		return status;
	}

	public void setStatus(MsgStatus status) {
		this.status = status;
	}

	public T getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}
}
