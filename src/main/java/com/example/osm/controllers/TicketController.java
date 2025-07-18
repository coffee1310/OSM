package com.example.osm.controllers;

import com.example.osm.entity.DTO.TicketDTO;
import com.example.osm.entity.Ticket;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicket(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.findById(id);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addTicket(@RequestBody Ticket ticket) {
        Optional<TicketDTO> ticketDTO = ticketService.createTicket(ticket);
        return ticketDTO.map(dto -> ResponseEntity
                .created(URI.create("/api/tickets/" + dto.getId()))
                .body(dto))
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putTicket(@PathVariable Long id, @RequestBody Ticket ticketDetails) {
        Ticket ticket = ticketService.findById(id)
                .orElseThrow(() ->
                new ResourceNotFound(String.format("Ticket with id: %d was not found", id)));
        Optional<TicketDTO> ticketDTO = ticketService.updateTicket(ticket, ticketDetails);
        return ticketDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        Ticket ticket = ticketService.findById(id).orElseThrow(() ->
                new ResourceNotFound(String.format("Ticket with id: %d was not found", id)));

        ticketService.deleteTicket(ticket);
        return new ResponseEntity<>("Ticket was deleted", HttpStatus.OK);
    }
}