package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Account {
    private final Long id;
    private final String email;
    private final String passwordHash;
    private final String role;
}
