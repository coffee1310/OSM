package com.example.osm.entity.DTO;

import com.example.osm.entity.User;
import lombok.Data;

@Data
public class TicketDTO {
    private Long id;
    private UserDTO user;
    private FlightDTO flight;
}
