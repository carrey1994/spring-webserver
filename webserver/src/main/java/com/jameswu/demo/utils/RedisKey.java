package com.jameswu.demo.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKey {
    LOCK_INIT_USERS("LOCK_INIT_USERS"),
    PREFIX_ADD_USER("LOCK_ADD_USER_");

    private final String key;
}
