package com.jameswu.demo.model.response;

import lombok.Data;

@Data
public class Result<T> {
	protected final T message;
	protected int code;

	public Result(T message, int code) {
		this.message = message;
		this.code = code;
	}

	public static <T> Result<T> success(T message) {
		return new Result<>(message, 200);
	}

	public static <T> Result<T> failure(T message, int code) {
		return new Result<>(message, code);
	}
}
