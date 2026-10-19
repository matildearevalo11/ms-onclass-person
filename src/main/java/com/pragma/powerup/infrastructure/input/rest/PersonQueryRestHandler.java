package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PersonIdsRequestDto;
import com.pragma.powerup.application.dto.response.PersonSummaryResponseDto;
import com.pragma.powerup.application.handler.IPersonQueryHandler;
import com.pragma.powerup.infrastructure.constants.ApiParameters;
import com.pragma.powerup.infrastructure.input.rest.support.QueryParams;
import com.pragma.powerup.infrastructure.input.rest.support.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PersonQueryRestHandler {
    private final IPersonQueryHandler handler;
    private final RequestValidator validator;

    public Mono<ServerResponse> findByIds(ServerRequest request) {
        return Mono.fromCallable(() -> new PersonIdsRequestDto(QueryParams.longList(request, ApiParameters.IDS)))
                .flatMap(validator::validate)
                .flatMap(query -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(handler.findByIds(query.getIds()), PersonSummaryResponseDto.class));
    }
}
