package com.valentin.reservacion_citas.web.exception;

public class CloudStorageException extends RuntimeException {
	public CloudStorageException(String message) {
		super(message);
	}

	public CloudStorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
