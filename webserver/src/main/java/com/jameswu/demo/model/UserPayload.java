package com.jameswu.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

public record UserPayload(
        @Size(min = 8, max = 16) String username,
        @Size(min = 8, max = 16) String password,
        @Email(message = "Invalid Email", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") String email,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC") Instant date,
        @Size(max = 25) String address,
        @Nullable Integer recommenderId)
        implements Serializable {}
