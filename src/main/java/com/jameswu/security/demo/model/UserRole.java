package com.jameswu.security.demo.model;

import static com.jameswu.security.demo.utils.GzTexts.ROLE_PREFIX;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public enum UserRole {
    USER(Set.of(Permission.READ_PRIVILEGE)),
    ADMIN(Set.of(Permission.READ_PRIVILEGE, Permission.WRITE_PRIVILEGE));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> authorities) {
        this.permissions = authorities;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + name()));
        return authorities;
    }
}
