package com.jameswu.demo.exception;

import com.jameswu.demo.model.response.Result;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GcControllerAdvice {

	@ExceptionHandler(value = {UsernameNotFoundException.class})
	public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
		return defaultException(ex, HttpStatus.NOT_FOUND, 430);
	}

	@ExceptionHandler(value = {IllegalArgumentException.class})
	public ResponseEntity<Object> handleIllegalException(IllegalArgumentException ex, WebRequest request) {
		return defaultException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request) {
		return defaultException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = {JwtException.class})
	public ResponseEntity<Object> handleJwtExceptions(Exception ex, WebRequest request) {
		return defaultException(ex, HttpStatus.FORBIDDEN, 432);
	}

	private ResponseEntity<Object> defaultException(Exception ex, HttpStatus httpStatus) {
		return new ResponseEntity<>(Result.failure(ex.getMessage(), httpStatus.value()), new HttpHeaders(), httpStatus);
	}

	private ResponseEntity<Object> defaultException(Exception ex, HttpStatus httpStatus, int customCode) {
		return new ResponseEntity<>(Result.failure(ex.getMessage(), customCode), new HttpHeaders(), httpStatus);
	}
}
