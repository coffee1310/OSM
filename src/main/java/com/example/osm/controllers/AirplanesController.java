package com.example.osm.controllers;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.repository.AirplaneRepository;
import com.example.osm.service.AirplaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/airplanes")
public class AirplanesController {

    @Autowired
    private AirplaneService airplaneService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPlane(@PathVariable Long id) {
        Optional<Airplane> plane = airplaneService.findById(id);
        return plane.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addPlane(@RequestBody Airplane plane) throws ResourceNotFound {
        Optional<AirplaneDTO> airplaneDTO = airplaneService.createAirplane(plane);
        return airplaneDTO.map(dto -> ResponseEntity
                        .created(URI.create("/api/airplanes/" + dto.getId()))
                        .body(dto))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putPlane(@PathVariable Long id, @RequestBody Airplane plane_details) throws ResourceNotFound {
        Airplane plane = airplaneService.findById(id)
                .orElseThrow(() -> new ResourceNotFound(String.format("Airplane with id: %d was not found", id)));
        Optional<AirplaneDTO> airplaneDTO = airplaneService.updateAirplane(plane, plane_details);

        return airplaneDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delPlane(@PathVariable Long id) throws ResourceNotFound {
        Airplane plane = airplaneService.findById(id)
                .orElseThrow(() -> new ResourceNotFound(String.format("Airplane with id: %d not found", id)));
        airplaneService.delete(plane);

        return new ResponseEntity<>("Airplane was deleted", HttpStatus.OK);
    }
}
