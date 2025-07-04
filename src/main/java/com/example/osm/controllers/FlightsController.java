package com.example.osm.controllers;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.AirplaneStatus;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.AirplaneService;
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

    @Autowired
    private AirplaneService airplaneService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getFlight(@PathVariable Long id) {
        Optional<Flight> flight = flightService.findById(id);

        if (flight.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FlightDTO flightDTO = flightService.convertToFlightDTO(flight.get());
        return new ResponseEntity<>(flightDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addFlight(@RequestBody Flight flight) {
        try {
            FlightDTO flightDTO = flightService.createFlight(flight);
            return new ResponseEntity<>(flightDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putFlight(@PathVariable Long id, @RequestBody Flight flightDetails) {
        try {
            Flight flight = flightService.findById(id).orElseThrow(() -> new ResourceNotFound(String.format("Flight with id: %d was not found", id)));

            FlightDTO flightDTO = flightService.putFlight(flight, flightDetails);
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
            return new ResponseEntity<>("Flight was deleted", HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
