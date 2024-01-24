package com.jameswu.security.demo.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Result<T> {
    private boolean ok;
    private T message;

    public Result(boolean ok, T message) {
        this.message = message;
        this.ok = ok;
    }

    public Result(T message) {
        this.ok = true;
        this.message = message;
    }
}
