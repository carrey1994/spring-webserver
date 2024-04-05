package com.jameswu.demo.model.payload;

import jakarta.validation.constraints.Size;
import java.io.Serializable;

public record SpecialsDetailPayload(@Size(min = 0) int inventory, @Size(min = 0) int booked)
		implements Serializable {}
