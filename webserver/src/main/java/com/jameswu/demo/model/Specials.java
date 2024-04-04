package com.jameswu.demo.model;

import java.io.Serializable;

public record Specials(int productId, SpecialsDetail specialsDetail) implements Serializable {}
