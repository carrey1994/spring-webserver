package com.jameswu.demo.exception;

import com.jameswu.demo.model.response.Result;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Hidden
@ControllerAdvice
public class GcControllerAdvice {

	@ExceptionHandler(value = {UsernameNotFoundException.class})
	public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		return defaultException(ex, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(value = {IllegalArgumentException.class})
	public ResponseEntity<Object> handleIllegalException(IllegalArgumentException ex) {
		return defaultException(ex, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@ExceptionHandler(value = {Exception.class})
	public ResponseEntity<Object> handleOtherExceptions(Exception ex) {
		return defaultException(ex, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@ExceptionHandler(value = {JwtException.class})
	public ResponseEntity<Object> handleJwtExceptions(JwtException ex) {
		return defaultException(ex, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Result<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest()
				.body(Result.failure(errors, HttpStatus.BAD_REQUEST.value(), "Validation failed"));
	}

	private ResponseEntity<Object> defaultException(Exception ex, HttpStatus httpStatus, int customCode) {
		String message = Optional.ofNullable(ex.getMessage()).orElse("No message");
		return new ResponseEntity<>(Result.failure(null, customCode, message), httpStatus);
	}
}
