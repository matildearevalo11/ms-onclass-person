package com.pragma.powerup.infrastructure.out.security;

import com.pragma.powerup.domain.spi.IPasswordVerifierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class BCryptPasswordVerifierAdapter implements IPasswordVerifierPort {
    private final PasswordEncoder encoder;

    @Override
    public Mono<Boolean> matches(String password, String hash) {
        return Mono.fromCallable(() -> encoder.matches(password, hash))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
