package com.pragma.powerup.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersonIdsRequestDto {
    @NotEmpty(message = ValidationMessages.PERSON_IDS_REQUIRED)
    private List<@NotNull(message = ValidationMessages.PERSON_ID_REQUIRED)
            @Positive(message = ValidationMessages.PERSON_ID_POSITIVE) Long> ids;
}
