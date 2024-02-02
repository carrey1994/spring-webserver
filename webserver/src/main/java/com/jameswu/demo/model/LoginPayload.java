package com.jameswu.demo.model;

import java.io.Serializable;

public record LoginPayload(String username, String password) implements Serializable {}
