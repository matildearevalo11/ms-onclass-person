package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.dto.response.TokenResponseDto;
import com.pragma.powerup.application.handler.IAuthenticationHandler;
import com.pragma.powerup.application.mapper.TokenResponseMapper;
import com.pragma.powerup.domain.api.IAuthenticationServicePort;
import lombok.RequiredArgsConstructor;
import com.pragma.powerup.domain.model.LoginCredentials;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthenticationHandler implements IAuthenticationHandler {
    private final IAuthenticationServicePort servicePort;
    private final TokenResponseMapper mapper;

    @Override
    public Mono<TokenResponseDto> login(LoginRequestDto request) {
        return servicePort.login(new LoginCredentials(request.getEmail(), request.getPassword())).map(mapper::toResponse);
    }
}
