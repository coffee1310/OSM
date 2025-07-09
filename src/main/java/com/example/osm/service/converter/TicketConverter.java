package com.example.osm.service.converter;

import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.DTO.TicketDTO;
import com.example.osm.entity.DTO.UserDTO;
import com.example.osm.entity.Flight;
import com.example.osm.entity.Ticket;
import com.example.osm.entity.User;

public class TicketConverter implements EntityConverter<Ticket, TicketDTO> {
    private UserConverter userConverter;
    private User user;
    private UserDTO userDTO;

    private FlightsConverter flightsConverter;
    private Flight flight;
    private FlightDTO flightDTO;

    @Override
    public Ticket toEntity(TicketDTO DTO) {
        DTO.validate();
        convertToUser(DTO.getUser());
        convertToFlight(DTO.getFlight());

        Ticket ticket = new Ticket();
        ticket.setId(DTO.getId());
        ticket.setUser(user);
        ticket.setUserId(user.getId());
        ticket.setFlight(flight);
        ticket.setFlightId(flight.getId());

        return ticket;
    }

    @Override
    public TicketDTO toDTO(Ticket entity) {
        convertToUserDTO(entity.getUser());
        convertToFlightDTO(entity.getFlight());

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(entity.getId());
        ticketDTO.setId(entity.getId());
        ticketDTO.setFlight(flightDTO);
        ticketDTO.setUser(userDTO);
        ticketDTO.validate();

        return ticketDTO;
    }

    private void convertToUser(UserDTO userDTO) {
        this.userConverter = new UserConverter();
        this.user = userConverter.toEntity(userDTO);
    }

    private void convertToUserDTO(User user) {
        this.userConverter = new UserConverter();
        this.userDTO = userConverter.toDTO(user);
    }

    private void convertToFlight(FlightDTO flightDTO) {
        this.flightsConverter = new FlightsConverter();
        this.flight = flightsConverter.toEntity(flightDTO);
    }

    private void convertToFlightDTO(Flight flight) {
        this.flightsConverter = new FlightsConverter();
        this.flightDTO = flightsConverter.toDTO(flight);
    }
}

