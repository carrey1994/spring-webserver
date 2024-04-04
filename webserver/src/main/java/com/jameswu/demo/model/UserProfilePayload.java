package com.jameswu.demo.model;

import java.io.Serializable;

public record UserProfilePayload(String email, String address) implements Serializable {}
