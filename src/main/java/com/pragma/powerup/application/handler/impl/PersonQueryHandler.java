package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.response.PersonSummaryResponseDto;
import com.pragma.powerup.application.handler.IPersonQueryHandler;
import com.pragma.powerup.application.mapper.PersonSummaryMapper;
import com.pragma.powerup.domain.api.IPersonQueryServicePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class PersonQueryHandler implements IPersonQueryHandler {
    private final IPersonQueryServicePort service;
    private final PersonSummaryMapper mapper;

    @Override
    public Flux<PersonSummaryResponseDto> findByIds(List<Long> ids) {
        return service.findByIds(ids).map(mapper::toResponse);
    }
}
