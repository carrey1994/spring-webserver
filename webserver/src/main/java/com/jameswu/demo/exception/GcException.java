package com.jameswu.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GcException {

	@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Token not found in redis")
	public static class TokenInvalidException extends IllegalArgumentException {
		public TokenInvalidException() {
			super("Token invalid");
		}
	}
}
