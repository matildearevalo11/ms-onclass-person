package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.TokenResponseDto;
import com.pragma.powerup.domain.model.AccessToken;
import org.springframework.stereotype.Component;

@Component
public class TokenResponseMapper {
    public TokenResponseDto toResponse(AccessToken token) {
        return new TokenResponseDto(token.accessToken(), token.tokenType(), token.expiresIn());
    }
}
