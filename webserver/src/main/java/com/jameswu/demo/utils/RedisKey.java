package com.jameswu.demo.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKey {
	LOCK_INIT_USERS("LOCK_INIT_USERS"),
	PREFIX_ADD_USER("LOCK_ADD_USER_"),
	PREFIX_CREATE_ORDER("LOCK_CREATE_ORDER_"),
	PREFIX_SPECIALS("SPECIALS_PRODUCT_ID_"),
	PREFIX_USER("USER_"),
	;

	private final String key;

	public static String withProductPrefix(int productId) {
		return PREFIX_SPECIALS.key + productId;
	}

	public static String withUserPrefix(int userId) {
		return PREFIX_SPECIALS.key + userId;
	}
}
