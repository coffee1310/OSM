package com.example.osm.entity.DTO;

import com.example.osm.entity.AirplaneStatus;
import jakarta.validation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class AirplaneDTO implements Validatable{
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 30)
    private String model;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 7)
    private String number;

    @NotNull
    AirplaneStatus status;

    @Override
    public void validate() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<AirplaneDTO>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
