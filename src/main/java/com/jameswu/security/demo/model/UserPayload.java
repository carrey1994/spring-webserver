package com.jameswu.security.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class UserPayload {
  private String username;
  private String password;
}
