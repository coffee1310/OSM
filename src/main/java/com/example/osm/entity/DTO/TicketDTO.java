package com.example.osm.entity.DTO;

import com.example.osm.entity.User;
import jakarta.validation.*;
import lombok.Data;

import java.util.Set;

@Data
public class TicketDTO implements Validatable {
    private Long id;

    @Valid
    private UserDTO user;

    @Valid
    private FlightDTO flight;

    @Override
    public void validate() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<TicketDTO>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
