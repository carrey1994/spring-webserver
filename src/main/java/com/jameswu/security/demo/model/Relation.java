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
