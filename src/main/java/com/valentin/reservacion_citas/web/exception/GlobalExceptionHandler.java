package com.valentin.reservacion_citas.web.exception;

import com.valentin.reservacion_citas.web.dto.response.MessageResDto;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<MessageResDto> handleBadRequestException(BadRequestException exception) {
		MessageResDto response = new MessageResDto(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<MessageResDto> handleUnauthorizedException(UnauthorizedException exception) {
		MessageResDto response = new MessageResDto(exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<MessageResDto> handleForbiddenException(ForbiddenException exception) {
		MessageResDto response = new MessageResDto(exception.getMessage(), HttpStatus.FORBIDDEN.value());
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(InternalServerException.class)
	public ResponseEntity<MessageResDto> handleInternalServerException(InternalServerException exception) {
		MessageResDto response = new MessageResDto(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<MessageResDto> handleNotFoundException(NotFoundException exception) {
		MessageResDto response = new MessageResDto(exception.getMessage(), HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<MessageResDto> handleConflictException(ConflictException exception) {
		MessageResDto response = new MessageResDto(exception.getMessage(), HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidCredentialException.class)
	public ResponseEntity<MessageResDto> handleInvalidCredentialException(InvalidCredentialException exception) {
		MessageResDto response = new MessageResDto(exception.getMessage(), HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
}
