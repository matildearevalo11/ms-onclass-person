package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IAuthenticationServicePort;
import com.pragma.powerup.domain.exception.AuthenticationException;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.model.AccessToken;
import com.pragma.powerup.domain.model.LoginCredentials;
import com.pragma.powerup.domain.spi.IAccountPersistencePort;
import com.pragma.powerup.domain.spi.IPasswordVerifierPort;
import com.pragma.powerup.domain.spi.ITokenProviderPort;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthenticationUseCase implements IAuthenticationServicePort {
    private final IAccountPersistencePort accounts;
    private final IPasswordVerifierPort passwords;
    private final ITokenProviderPort tokens;

    @Override
    public Mono<AccessToken> login(LoginCredentials credentials) {
        return Mono.defer(() -> accounts.findByEmail(credentials.getEmail().strip().toLowerCase(Locale.ROOT))
                    .switchIfEmpty(Mono.error(new AuthenticationException(ExceptionMessages.INVALID_CREDENTIALS.getMessage())))
                    .flatMap(account -> passwords.matches(credentials.getPassword(), account.getPasswordHash())
                            .flatMap(matches -> Boolean.TRUE.equals(matches) ? tokens.generate(account)
                                    : Mono.error(new AuthenticationException(ExceptionMessages.INVALID_CREDENTIALS.getMessage())))));
    }
}
