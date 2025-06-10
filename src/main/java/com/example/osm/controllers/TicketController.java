package com.example.osm.controllers;

import com.example.osm.entity.DTO.TicketDTO;
import com.example.osm.entity.Ticket;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicket(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);
        TicketDTO ticketDTO = ticketService.convertToTicketDTO(ticket.get());
        return new ResponseEntity<>(ticketDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addTicket(@RequestBody Ticket ticket) {
        try {
            try {
                ticketService.createTicket(ticket);
                return new ResponseEntity<>(ticketService.convertToTicketDTO(ticket), HttpStatus.CREATED);
            } catch (ResourceNotFound e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putTicket(@PathVariable Long id, @RequestBody Ticket ticketDetails) {
        try {
            Ticket ticket = ticketService.findById(id)
                    .orElseThrow(() ->
                    new ResourceNotFound(String.format("Ticket with id: %d was not found", id)));
            TicketDTO new_ticket = ticketService.updateTicket(ticket, ticketDetails);
            return new ResponseEntity<>(new_ticket, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.findById(id)
                    .orElseThrow(() ->
                    new ResourceNotFound(String.format("Ticket with id: %d was not found", id)));
            ticketService.deleteTicket(ticket);
            return new ResponseEntity<>("Ticket was deleted", HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}