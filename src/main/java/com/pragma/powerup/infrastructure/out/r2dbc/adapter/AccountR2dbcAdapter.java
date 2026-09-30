package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.model.Account;
import com.pragma.powerup.domain.spi.IAccountPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.mapper.AccountEntityMapper;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AccountR2dbcAdapter implements IAccountPersistencePort {
    private final IAccountRepository repository;
    private final AccountEntityMapper mapper;

    @Override
    public Mono<Account> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toModel);
    }
}
