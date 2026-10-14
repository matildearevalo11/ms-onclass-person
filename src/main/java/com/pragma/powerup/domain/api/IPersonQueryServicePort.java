package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.PersonSummary;
import java.util.List;
import reactor.core.publisher.Flux;

public interface IPersonQueryServicePort {
    Flux<PersonSummary> findByIds(List<Long> ids);
}
