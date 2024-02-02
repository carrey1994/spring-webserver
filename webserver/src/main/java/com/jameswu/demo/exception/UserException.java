package com.jameswu.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserException extends IllegalArgumentException {

    public UserException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserException(String msg) {
        super(msg);
    }
}
