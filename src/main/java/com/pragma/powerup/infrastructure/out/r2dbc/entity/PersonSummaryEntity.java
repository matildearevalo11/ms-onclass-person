package com.pragma.powerup.infrastructure.out.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersonSummaryEntity {
    private final Long id;
    private final String name;
    private final String email;
}
