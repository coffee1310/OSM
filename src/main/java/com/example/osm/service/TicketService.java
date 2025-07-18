package com.example.osm.service;

import com.example.osm.entity.DTO.TicketDTO;
import com.example.osm.entity.Flight;
import com.example.osm.entity.Ticket;
import com.example.osm.entity.User;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.repository.TicketRepository;
import com.example.osm.service.DTOConverter.DTOConverterFactory;
import com.example.osm.service.DTOConverter.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private final FlightService flightService;

    @Autowired
    private final UserService userService;

    private final DTOConverterFactory converterFactory;

    @Autowired
    public TicketService(TicketRepository ticketRepository, FlightService flightService, UserService userService, DTOConverterFactory converterFactory) {
        this.ticketRepository = ticketRepository;
        this.flightService = flightService;
        this.userService = userService;
        this.converterFactory = converterFactory;
    }

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public Optional<TicketDTO> createTicket(Ticket ticket) throws ResourceNotFound {
        Flight flight = flightService.findById(ticket.getFlightId())
                .orElseThrow(() ->
                new ResourceNotFound(String.format("Flight with id: %d was not found", ticket.getFlightId())));

        User user = userService.findById(ticket.getUserId())
                .orElseThrow(() ->
                new ResourceNotFound(String.format("User with id: %d was not found", ticket.getUserId())));

        ticket.setFlight(flight);
        ticket.setUser(user);
        ticketRepository.save(ticket);

        EntityConverter<Ticket, TicketDTO> converter = converterFactory.getConverter(Ticket.class, TicketDTO.class);

        TicketDTO ticketDTO = converter.toDTO(ticket);
        return Optional.of(ticketDTO);
    }

    public Optional<TicketDTO> updateTicket(Ticket ticket, Ticket ticketDetails) throws ResourceNotFound{
        Long user_id = ticketDetails.getUserId();
        Long flight_id = ticketDetails.getFlightId();

        User user = userService.findById(user_id)
                .orElseThrow(() -> new ResourceNotFound(String.format("user with id: %d was not found", user_id)));
        Flight flight = flightService.findById(flight_id)
                .orElseThrow(() -> new ResourceNotFound(String.format("Flight with id: %d was not found", flight_id)));
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticketRepository.save(ticket);

        EntityConverter<Ticket, TicketDTO> converter = converterFactory
                .getConverter(Ticket.class, TicketDTO.class);

        TicketDTO ticketDTO = converter.toDTO(ticket);
        return Optional.of(ticketDTO);
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }
}
