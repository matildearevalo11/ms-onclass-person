package com.pragma.powerup.domain.spi;

import reactor.core.publisher.Mono;

public interface IPasswordVerifierPort {
    Mono<Boolean> matches(String password, String hash);
}
