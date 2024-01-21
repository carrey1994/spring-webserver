package com.jameswu.security.demo.model;

import org.springframework.security.core.GrantedAuthority;

// @Embeddable
public enum UserAuthority implements GrantedAuthority {
    USER,
    ADMIN;
    private String authority;

    @Override
    public java.lang.String getAuthority() {
        return this.authority;
    }
}
