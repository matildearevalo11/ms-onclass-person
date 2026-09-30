package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record AccountCredentialsEntity(@Id Long id, String email, String passwordHash, String role) {
}
