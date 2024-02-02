package com.jameswu.demo.model.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GcProfileLevel {
    private long userId;
    private String email;
    private String address;
    private LocalDate enrollmentDate;
    private Long recommenderId;
    private int level;

    public UserProfile toUserProfile() {
        return new UserProfile(userId, email, address, enrollmentDate, recommenderId);
    }
}
