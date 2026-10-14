package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.PersonSummaryResponseDto;
import com.pragma.powerup.domain.model.PersonSummary;
import org.springframework.stereotype.Component;

@Component
public class PersonSummaryMapper {
    public PersonSummaryResponseDto toResponse(PersonSummary person) {
        return new PersonSummaryResponseDto(person.getId(), person.getName(), person.getEmail());
    }
}
