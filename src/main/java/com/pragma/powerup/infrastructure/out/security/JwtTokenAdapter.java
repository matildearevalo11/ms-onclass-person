package com.pragma.powerup.infrastructure.out.security;

import com.pragma.powerup.infrastructure.constants.SecurityConstants;
import com.pragma.powerup.domain.model.AccessToken;
import com.pragma.powerup.domain.model.Account;
import com.pragma.powerup.domain.spi.ITokenProviderPort;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
public class JwtTokenAdapter implements ITokenProviderPort {
    private final JwtEncoder encoder;
    private final String issuer;
    private final String audience;
    private final Duration expiration;

    @Override
    public Mono<AccessToken> generate(Account account) {
        return Mono.fromCallable(() -> {
            Instant now = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder().issuer(issuer)
                    .subject(account.getId().toString()).audience(List.of(audience))
                    .issuedAt(now).expiresAt(now.plus(expiration))
                    .claim(SecurityConstants.ROLE_CLAIM, account.getRole()).build();
            JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
            String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
            return new AccessToken(token, SecurityConstants.BEARER, expiration.toSeconds());
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
