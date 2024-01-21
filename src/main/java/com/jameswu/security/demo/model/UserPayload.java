package com.jameswu.security.demo.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPayload {
    @Size(min = 8, max = 16)
    private String username;

    @Size(min = 8, max = 16)
    private String password;
}
