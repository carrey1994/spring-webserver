package com.jameswu.demo.model;

import java.io.Serializable;

public record SpecialsDetail(int inventory, int booked) implements Serializable {}
