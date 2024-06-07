package com.jameswu.demo.model.payload;

import com.jameswu.demo.annotation.Password;
import java.io.Serializable;

public record LoginPayload(@Password String username, @Password String password) implements Serializable {}
