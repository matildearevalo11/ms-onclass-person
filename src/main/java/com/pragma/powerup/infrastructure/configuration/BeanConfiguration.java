package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IAuthenticationServicePort;
import com.pragma.powerup.domain.spi.IAccountPersistencePort;
import com.pragma.powerup.domain.spi.IPasswordVerifierPort;
import com.pragma.powerup.domain.spi.ITokenProviderPort;
import com.pragma.powerup.domain.usecase.AuthenticationUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {
    @Bean
    IAuthenticationServicePort authenticationServicePort(IAccountPersistencePort accounts,
            IPasswordVerifierPort passwords, ITokenProviderPort tokens) {
        return new AuthenticationUseCase(accounts, passwords, tokens);
    }

    @Bean
    PasswordEncoder passwordEncoder(@Value("${security.password.bcrypt-strength}") int strength) {
        return new BCryptPasswordEncoder(strength);
    }
}
