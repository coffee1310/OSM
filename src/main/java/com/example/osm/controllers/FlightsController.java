package com.example.osm.controllers;

import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.AirplaneService;
import com.example.osm.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
public class FlightsController {
    @Autowired
    private FlightService flightService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getFlight(@PathVariable Long id) {
        Optional<Flight> flight = flightService.findById(id);
        return flight.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping
    public ResponseEntity<?> addFlight(@RequestBody Flight flight) {
       Optional<FlightDTO> flightDTO = flightService.createFlight(flight);
       return flightDTO.map(dto -> ResponseEntity
                       .created(URI.create("/api/flights/" + dto.getId()))
                       .body(dto))
               .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putFlight(@PathVariable Long id, @RequestBody Flight flightDetails) {
        Flight flight = flightService.findById(id).orElseThrow(()
                -> new ResourceNotFound(String.format("Flight with id: %d was not found", id)));

        Optional<FlightDTO> flightDTO = flightService.putFlight(flight, flightDetails);
        return flightDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
        Flight flight = flightService.findById(id).orElseThrow(()
                -> new ResourceNotFound(String.format("Flight with id: %d was not found", id)));

        flightService.delete(flight);
        return new ResponseEntity<>("Flight was deleted", HttpStatus.OK);
    }
}
