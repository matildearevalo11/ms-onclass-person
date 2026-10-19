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
import static com.pragma.powerup.domain.util.DomainValidations.requireText;

@RequiredArgsConstructor
public class AuthenticationUseCase implements IAuthenticationServicePort {
    private final IAccountPersistencePort accounts;
    private final IPasswordVerifierPort passwords;
    private final ITokenProviderPort tokens;

    @Override
    public Mono<AccessToken> login(LoginCredentials credentials) {
        return Mono.fromCallable(() -> validate(credentials))
                .flatMap(valid -> accounts.findByEmail(valid.getEmail())
                        .switchIfEmpty(Mono.error(invalidCredentials()))
                        .flatMap(account -> passwords.matches(valid.getPassword(), account.getPasswordHash())
                                .flatMap(matches -> Boolean.TRUE.equals(matches)
                                        ? tokens.generate(account) : Mono.error(invalidCredentials()))));
    }

    private LoginCredentials validate(LoginCredentials credentials) {
        String email = requireText(credentials.getEmail(), ExceptionMessages.CREDENTIALS_REQUIRED);
        requireText(credentials.getPassword(), ExceptionMessages.CREDENTIALS_REQUIRED);
        return new LoginCredentials(email.toLowerCase(Locale.ROOT), credentials.getPassword());
    }

    private AuthenticationException invalidCredentials() {
        return new AuthenticationException(ExceptionMessages.INVALID_CREDENTIALS.getMessage());
    }
}
