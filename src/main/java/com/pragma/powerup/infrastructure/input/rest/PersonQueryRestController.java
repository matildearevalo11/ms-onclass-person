package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.response.PersonSummaryResponseDto;
import com.pragma.powerup.application.handler.IPersonQueryHandler;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
@Validated
public class PersonQueryRestController {
    private final IPersonQueryHandler handler;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<PersonSummaryResponseDto> findByIds(@RequestParam @NotEmpty List<@Positive Long> ids) {
        return handler.findByIds(ids);
    }
}
