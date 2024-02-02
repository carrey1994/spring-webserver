package com.jameswu.demo.model.response;

public final class FailureResult<T> extends Result<T> {
    public FailureResult(T message) {
        super(false, message);
    }
}
