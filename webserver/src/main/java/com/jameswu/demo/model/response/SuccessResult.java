package com.jameswu.demo.model.response;

public final class SuccessResult<T> extends Result<T> {
    public SuccessResult(T message) {
        super(true, message);
    }
}
