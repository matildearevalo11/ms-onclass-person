package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.domain.exception.AuthenticationException;
import com.pragma.powerup.domain.model.LoginCredentials;
import reactor.core.publisher.Mono;
import static org.mockito.Mockito.when;
import com.pragma.powerup.domain.spi.IAccountPersistencePort;
import com.pragma.powerup.domain.spi.IPasswordVerifierPort;
import com.pragma.powerup.domain.spi.ITokenProviderPort;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class AuthenticationUseCaseTest {
    @Test
    void rejectsUnknownAccountWithoutVerifyingPasswordOrIssuingToken() {
        var accounts = mock(IAccountPersistencePort.class);
        var passwords = mock(IPasswordVerifierPort.class);
        var tokens = mock(ITokenProviderPort.class);
        var useCase = new AuthenticationUseCase(accounts, passwords, tokens);

        when(accounts.findByEmail("missing@example.com")).thenReturn(Mono.empty());
        StepVerifier.create(useCase.login(new LoginCredentials("missing@example.com", "password")))
                .expectErrorMatches(error -> error instanceof AuthenticationException
                        && error.getMessage().equals(ExceptionMessages.INVALID_CREDENTIALS.getMessage()))
                .verify();
        verifyNoInteractions(passwords, tokens);
    }

    @Test
    void requiresEmailAndPasswordBeforeQueryingAccounts() {
        var accounts = mock(IAccountPersistencePort.class);
        var passwords = mock(IPasswordVerifierPort.class);
        var tokens = mock(ITokenProviderPort.class);
        var useCase = new AuthenticationUseCase(accounts, passwords, tokens);

        for (var credentials : java.util.List.of(new LoginCredentials(" ", "secret"),
                new LoginCredentials("admin@example.com", ""), new LoginCredentials(null, null))) {
            StepVerifier.create(useCase.login(credentials))
                    .expectErrorMatches(error -> error instanceof com.pragma.powerup.domain.exception.ValidationException
                            && error.getMessage().equals(ExceptionMessages.CREDENTIALS_REQUIRED.getMessage()))
                    .verify();
        }
        verifyNoInteractions(accounts, passwords, tokens);
    }

    @Test
    void normalizesEmailButKeepsPasswordUntouched() {
        var accounts = mock(IAccountPersistencePort.class);
        var passwords = mock(IPasswordVerifierPort.class);
        var tokens = mock(ITokenProviderPort.class);
        when(accounts.findByEmail("admin@example.com")).thenReturn(Mono.empty());

        StepVerifier.create(new AuthenticationUseCase(accounts, passwords, tokens)
                        .login(new LoginCredentials(" ADMIN@example.com ", " secret ")))
                .expectError(AuthenticationException.class).verify();
        verify(accounts).findByEmail("admin@example.com");
    }
}
