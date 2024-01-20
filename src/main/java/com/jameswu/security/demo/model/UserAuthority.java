package com.jameswu.security.demo.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority {
  USER;
  private String getAuthority;

  @Override
  public java.lang.String getAuthority() {
    return null;
  }
}
