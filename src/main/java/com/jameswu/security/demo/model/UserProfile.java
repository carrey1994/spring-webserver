package com.jameswu.security.demo.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "user_profile")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserProfile {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column
    @Email
    private String email;

    @Column
    @NotNull
    private String address;

    @Column
    private LocalDate enrollmentDate;

    /* If member joins by himself, recommenderId assigned null. */
    @Column
    @Nullable
    private UUID recommenderId;
}
