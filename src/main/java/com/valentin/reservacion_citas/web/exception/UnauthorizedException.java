package com.valentin.reservacion_citas.web.exception;

public class UnauthorizedException extends RuntimeException {
	public UnauthorizedException(String message) {
		super(message);
	}
}
