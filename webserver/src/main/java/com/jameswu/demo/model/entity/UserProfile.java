package com.jameswu.demo.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_profile")
@Builder
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private long userId;

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
    private Long recommenderId;
}
