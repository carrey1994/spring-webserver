package com.jameswu.security.demo.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation {

    @EmbeddedId
    private RelationId relationId;

    /* distance between each other */
    public Relation(UUID ancestorId, UUID descendantId) {
        relationId = new RelationId(ancestorId, descendantId);
    }
}

/**
 * Example
 *
 * @user1(1,1,0)
 * @user2(2,1,1) invited by user1
 * @user3(3,2,2) invited by user2
 * @user4(4,1,1) invited by user1
 */
