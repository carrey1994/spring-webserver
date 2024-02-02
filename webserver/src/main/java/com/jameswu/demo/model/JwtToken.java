package com.jameswu.demo.model;

import jakarta.persistence.Id;
import java.io.Serializable;

public record JwtToken(@Id String token) implements Serializable {}
