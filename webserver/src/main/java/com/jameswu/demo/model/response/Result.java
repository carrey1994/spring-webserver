package com.jameswu.demo.model.response;

import lombok.Data;

@Data
public sealed class Result<T> permits SuccessResult, FailureResult {
    protected final boolean ok;
    protected final T message;

    public Result(boolean ok, T message) {
        this.ok = ok;
        this.message = message;
    }
}
