package com.jameswu.security.demo.model;

import java.util.Set;

public enum UserRole {
    USER(Set.of(Permission.READ_PRIVILEGE)),
    ADMIN(Set.of(Permission.READ_PRIVILEGE, Permission.WRITE_PRIVILEGE));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> authorities) {
        this.permissions = authorities;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
