package com.jameswu.demo.model.response;

import lombok.Data;

@Data
public class Result<T> {
	protected final T result;
	protected String message;
	protected int code;

	public Result(T result, int code, String message) {
		this.result = result;
		this.code = code;
		this.message = message;
	}

	public static <T> Result<T> success(T result) {
		return new Result<>(result, 200, null);
	}

	public static <T> Result<T> failure(T result, int code, String message) {
		return new Result<>(result, code, message);
	}
}
