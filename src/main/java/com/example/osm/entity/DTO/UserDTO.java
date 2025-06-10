package com.example.osm.entity.DTO;

import jakarta.validation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;

    @Size(min = 5)
    private String fullName;

    @Min(1)
    @Max(150)
    private short age;

    public void validate() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
