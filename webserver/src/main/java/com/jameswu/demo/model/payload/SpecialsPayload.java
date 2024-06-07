package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public record SpecialsPayload(@Min(0) int productId, @NotNull SpecialsDetailPayload specialsDetailPayload)
		implements Serializable {}
