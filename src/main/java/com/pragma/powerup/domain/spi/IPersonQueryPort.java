package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.PersonSummary;
import java.util.List;
import reactor.core.publisher.Flux;

public interface IPersonQueryPort {
    Flux<PersonSummary> findByIds(List<Long> ids);
}
