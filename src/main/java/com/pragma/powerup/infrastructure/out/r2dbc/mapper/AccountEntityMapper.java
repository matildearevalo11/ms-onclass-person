package com.pragma.powerup.infrastructure.out.r2dbc.mapper;

import com.pragma.powerup.domain.model.Account;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.AccountCredentialsEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountEntityMapper {
    public Account toModel(AccountCredentialsEntity entity) {
        return new Account(entity.id(), entity.email(), entity.passwordHash(), entity.role());
    }
}
