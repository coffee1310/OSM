package com.example.osm.controllers;

import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
public class FlightsController {
    @Autowired
    private FlightService flightService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getFlight(@PathVariable Long id) {
        Optional<Flight> flight = flightService.findById(id);
        FlightDTO flightDTO = new FlightDTO();
        if (flight.isPresent()) flightDTO = flightService.convertToFlightDTO(flight.get());

        return new ResponseEntity<>(flightDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addFlight(@RequestBody Flight flight) {
        try {
            flight = flightService.createFlight(flight);
            return new ResponseEntity<>(flight, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putFlight(@PathVariable Long id, @RequestBody Flight flightDetails) {
        try {
            Flight flight = flightService.findById(id).orElseThrow(() -> new ResourceNotFound(String.format("Flight with id: %d was not found", id)));

            flight.setAirplane(flightDetails.getAirplane());
            flight.setStartTime(flightDetails.getStartTime());
            flight.setEndTime(flightDetails.getEndTime());
            flight.setStartPlace(flightDetails.getStartPlace());
            flight.setEndPlace(flightDetails.getEndPlace());
            flight.setAirplaneId(flightDetails.getAirplaneId());

            flight = flightService.save(flight);
            FlightDTO flightDTO = flightService.convertToFlightDTO(flight);
            return new ResponseEntity<>(flightDTO, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable Long id) {
        try {
            Flight flight = flightService.findById(id).orElseThrow(() -> new ResourceNotFound(String.format("Flight with id: %d was not found", id)));

            flightService.delete(flight);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
