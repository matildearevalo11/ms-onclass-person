package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.model.PersonSummary;
import com.pragma.powerup.domain.spi.IPersonQueryPort;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.IAccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class PersonQueryR2dbcAdapter implements IPersonQueryPort {
    private final IAccountRepository repository;

    @Override
    public Flux<PersonSummary> findByIds(List<Long> ids) {
        return repository.findProfilesByIds(ids)
                .map(person -> new PersonSummary(person.getId(), person.getName(), person.getEmail()));
    }
}
