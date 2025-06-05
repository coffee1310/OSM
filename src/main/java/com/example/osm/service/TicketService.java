package com.example.osm.service;

import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.DTO.TicketDTO;
import com.example.osm.entity.DTO.UserDTO;
import com.example.osm.entity.Flight;
import com.example.osm.entity.Ticket;
import com.example.osm.entity.User;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    FlightService flightService;

    @Autowired
    UserService userService;

    public Optional<Ticket> findById(Long id) {
        return ticketRepository.findById(id);
    }

    public TicketDTO createTicket(Ticket ticket) throws ResourceNotFound {
        Flight flight = flightService.findById(ticket.getFlightId())
                .orElseThrow(() ->
                new ResourceNotFound(String.format("Flight with id: %d was not found", ticket.getFlightId())));
        User user = userService.findById(ticket.getUserId())
                .orElseThrow(() ->
                new ResourceNotFound(String.format("User with id: %d was not found", ticket.getUserId())));

        ticket.setFlight(flight);
        ticket.setUser(user);
        ticketRepository.save(ticket);

        TicketDTO ticketDTO = convertToTicketDTO(ticket);
        return ticketDTO;
    }

    public Ticket updateTicket(Ticket ticket, Ticket ticketDetails) throws ResourceNotFound{
        Long user_id = ticketDetails.getUserId();
        Long flight_id = ticketDetails.getFlightId();

        User user = userService.findById(user_id)
                .orElseThrow(() -> new ResourceNotFound(String.format("user with id: %d was not found", user_id)));
        Flight flight = flightService.findById(flight_id)
                .orElseThrow(() -> new ResourceNotFound(String.format("Flight with id: %d was not found", flight_id)));
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticketRepository.save(ticket);

        return ticket;
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public TicketDTO convertToTicketDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        FlightDTO flightDTO;
        UserDTO userDTO;

        flightDTO = flightService.convertToFlightDTO(ticket.getFlight());
        userDTO = userService.convertToUserDTO(ticket.getUser());

        ticketDTO.setId(ticket.getId());
        ticketDTO.setFlight(flightDTO);
        ticketDTO.setUser(userDTO);
        return ticketDTO;
    }
}
