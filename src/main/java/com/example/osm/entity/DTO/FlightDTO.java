package com.example.osm.entity.DTO;

import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class FlightDTO implements Validatable {
    private Long id;

    @NotNull
    @Size(min = 3)
    private String startPlace;

    @NotNull
    @Size(min = 3)
    private String endPlace;

    @FutureOrPresent
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;

    @Valid
    private AirplaneDTO airplane;

    @Override
    public void validate() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<FlightDTO>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
