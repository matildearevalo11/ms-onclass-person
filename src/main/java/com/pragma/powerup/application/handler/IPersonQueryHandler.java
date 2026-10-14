package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.response.PersonSummaryResponseDto;
import java.util.List;
import reactor.core.publisher.Flux;

public interface IPersonQueryHandler {
    Flux<PersonSummaryResponseDto> findByIds(List<Long> ids);
}
