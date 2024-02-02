package com.jameswu.demo.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    READ_PRIVILEGE("READ_PRIVILEGE"),
    WRITE_PRIVILEGE("WRITE_PRIVILEGE");

    private final String privilege;
}
