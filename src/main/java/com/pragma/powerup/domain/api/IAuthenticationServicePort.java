package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.AccessToken;
import com.pragma.powerup.domain.model.LoginCredentials;
import reactor.core.publisher.Mono;

public interface IAuthenticationServicePort {
    Mono<AccessToken> login(LoginCredentials credentials);
}
