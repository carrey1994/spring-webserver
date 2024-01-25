package com.jameswu.security.demo.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationId {
    /* recommender user id*/
    @NotNull
    private UUID ancestorId;

    /* invitee user id */
    @NotNull
    private UUID descendantId;
}
