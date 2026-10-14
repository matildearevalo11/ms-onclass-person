package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Account;
import reactor.core.publisher.Mono;

public interface IAccountPersistencePort {
    Mono<Account> findByEmail(String email);
}
