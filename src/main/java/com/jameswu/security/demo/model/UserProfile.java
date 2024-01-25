package com.jameswu.security.demo.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity(name = "user_profile")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(indexes = {@Index(name = "user_id_index", columnList = "user_id")})
public class UserProfile {
    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column
    @Email
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDate enrollmentDate;

    /* If member joins by himself, recommenderId assigned null. */
    @Column
    @Nullable
    private UUID recommenderId;
}
