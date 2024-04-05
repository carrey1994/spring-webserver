package com.jameswu.demo.model.response;

import lombok.Data;

@Data
public class Result<T> {
	protected final boolean ok;
	protected final T message;

	public Result(boolean ok, T message) {
		this.ok = ok;
		this.message = message;
	}

	public static <T> Result<T> success(T message) {
		return new Result<>(true, message);
	}

	public static <T> Result<T> failure(T message) {
		return new Result<>(false, message);
	}
}
