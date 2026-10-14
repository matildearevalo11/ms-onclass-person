package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPersonQueryServicePort;
import com.pragma.powerup.domain.model.PersonSummary;
import com.pragma.powerup.domain.spi.IPersonQueryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class PersonQueryUseCase implements IPersonQueryServicePort {
    private final IPersonQueryPort queryPort;

    @Override
    public Flux<PersonSummary> findByIds(List<Long> ids) {
        return queryPort.findByIds(ids);
    }
}
