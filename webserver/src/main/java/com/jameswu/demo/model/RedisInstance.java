package com.jameswu.demo.model;

import java.io.Serializable;

public record RedisInstance(String host, int port, String password, String username) implements Serializable {}
