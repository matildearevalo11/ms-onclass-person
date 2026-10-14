package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.AccessToken;
import com.pragma.powerup.domain.model.Account;
import reactor.core.publisher.Mono;

public interface ITokenProviderPort {
    Mono<AccessToken> generate(Account account);
}
