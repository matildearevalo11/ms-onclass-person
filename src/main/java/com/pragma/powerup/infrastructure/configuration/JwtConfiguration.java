package com.pragma.powerup.infrastructure.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.pragma.powerup.domain.spi.ITokenProviderPort;
import com.pragma.powerup.infrastructure.out.security.JwtTokenAdapter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfiguration {
    @Bean
    JwtEncoder jwtEncoder(@Value("${jwt.secret}") String secret) {
        var key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    @Bean
    ITokenProviderPort tokenProviderPort(JwtEncoder encoder,
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.audience}") String audience,
            @Value("${jwt.expiration}") Duration expiration) {
        return new JwtTokenAdapter(encoder, issuer, audience, expiration);
    }
}
