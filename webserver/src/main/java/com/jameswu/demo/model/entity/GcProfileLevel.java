package com.jameswu.demo.model.entity;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GcProfileLevel implements Serializable {
    private long userId;
    private String email;
    private String address;
    private Instant enrollmentDate;
    private Long recommenderId;
    private int level;

    public UserProfile toUserProfile() {
        return new UserProfile(userId, email, address, enrollmentDate, recommenderId);
    }
}
