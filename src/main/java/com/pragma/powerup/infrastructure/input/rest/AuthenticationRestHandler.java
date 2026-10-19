package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.handler.IAuthenticationHandler;
import com.pragma.powerup.infrastructure.input.rest.support.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationRestHandler {
    private final IAuthenticationHandler handler;
    private final RequestValidator validator;

    public Mono<ServerResponse> login(ServerRequest request) {
        return validator.body(request, LoginRequestDto.class)
                .flatMap(handler::login)
                .flatMap(token -> ServerResponse.ok().cacheControl(CacheControl.noStore())
                        .contentType(MediaType.APPLICATION_JSON).bodyValue(token));
    }
}
