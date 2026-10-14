package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.dto.response.TokenResponseDto;
import reactor.core.publisher.Mono;

public interface IAuthenticationHandler {
    Mono<TokenResponseDto> login(LoginRequestDto request);
}
