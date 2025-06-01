package com.example.osm.controllers;

import com.example.osm.entity.Airplane;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.repository.AirplaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/airplanes")
public class AirplanesController {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlane(@PathVariable Long id) {
        Optional<Airplane> plane = airplaneRepository.findById(id);
        return plane.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addPlane(@RequestBody Airplane plane) {
        try {
            Airplane new_plane = airplaneRepository.save(plane);
            return new ResponseEntity<>(new_plane, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putPlane(@PathVariable Long id, @RequestBody Airplane plane_details) {
        try {
            Airplane plane = airplaneRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFound(String.format("Airplane with id: %d not found", id)));
            plane.setModel(plane_details.getModel());
            plane.setNumber(plane_details.getNumber());
            plane.setStatus(plane_details.getStatus());
            plane.setFlights(plane_details.getFlights());

            plane = airplaneRepository.save(plane);

            return new ResponseEntity<>(plane, HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delPlane(@PathVariable Long id) {
        try {
            Airplane plane = airplaneRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFound(String.format("Airplane with id: %d not found", id)));
            airplaneRepository.delete(plane);

            return new ResponseEntity<>("Airplane was deleted", HttpStatus.OK);
        } catch (ResourceNotFound e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }
}
