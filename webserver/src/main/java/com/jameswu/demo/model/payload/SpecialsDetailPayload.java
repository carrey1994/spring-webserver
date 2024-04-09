package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.Min;
import java.io.Serializable;

public record SpecialsDetailPayload(@Min(0) int inventory, @Min(0) int booked)
		implements Serializable {}
