package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public record SpecialsPayload(@Size(min = 0) int productId, @NotNull SpecialsDetailPayload specialsDetailPayload)
        implements Serializable {}
