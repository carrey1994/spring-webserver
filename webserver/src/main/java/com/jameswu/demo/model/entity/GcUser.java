package com.jameswu.demo.model.entity;

import com.jameswu.demo.model.enums.UserRole;
import com.jameswu.demo.model.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NamedEntityGraph(
        name = "gc_user_graph",
        attributeNodes = {@NamedAttributeNode("profile")})
@Entity(name = "gc_user")
@Builder
@Table(indexes = {@Index(name = "idx_username", columnList = "username", unique = true)})
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class GcUser implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @Column
    @Size(min = 8, max = 16)
    private String username;

    @Column
    @NotBlank
    private String password;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private UserRole userRole;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserProfile profile;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private UserStatus userStatus;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRole.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userStatus.equals(UserStatus.ACTIVE);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GcUser gcUser = (GcUser) object;
        return userId == gcUser.userId
                && Objects.equals(username, gcUser.username)
                && Objects.equals(password, gcUser.password)
                && userRole == gcUser.userRole
                && Objects.equals(profile, gcUser.profile)
                && userStatus == gcUser.userStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, userRole, profile, userStatus);
    }
}
