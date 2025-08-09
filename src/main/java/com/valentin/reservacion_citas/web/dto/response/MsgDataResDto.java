package com.valentin.reservacion_citas.web.dto.response;

public class MsgDataResDto<T> {
	private MsgStatus status;

	private String message;

	private T data;

	public MsgStatus getStatus() {
		return status;
	}

	public void setStatus(MsgStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
