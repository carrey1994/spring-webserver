package com.jameswu.security.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GcProfileWithRelation {
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

    @Column(nullable = false)
    private int level;

    @NotNull
    @Column(nullable = false)
    private UUID ancestorId;

    @NotNull
    @Column(nullable = false)
    private UUID descendantId;

    @Column(nullable = false)
    private UUID recommender_id;
}
