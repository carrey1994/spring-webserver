package com.jameswu.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_profile")
@Builder
@Table
@Getter
@Setter
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
    @JsonFormat(shape = JsonFormat.Shape.NUMBER, without = JsonFormat.Feature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
    private Instant enrollmentDate;

    /* If member joins by himself, recommenderId assigned null. */
    @Column
    @Nullable
    private Long recommenderId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        UserProfile that = (UserProfile) object;
        return userId == that.userId
                && Objects.equals(email, that.email)
                && Objects.equals(address, that.address)
                && Objects.equals(enrollmentDate, that.enrollmentDate)
                && Objects.equals(recommenderId, that.recommenderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, address, enrollmentDate, recommenderId);
    }
}
