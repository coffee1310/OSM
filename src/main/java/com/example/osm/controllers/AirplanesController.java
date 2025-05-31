package com.example.osm.controllers;

import com.example.osm.Airplane;
import com.example.osm.repository.AirplaneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airplanes")
@Transactional
public class AirplanesController {

    @Autowired
    private AirplaneRepository airplaneRepository;


    @GetMapping("/{id}")
    public ResponseEntity<?> getPlane(@PathVariable Long id) {
        try {
            Airplane plane = airplaneRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(plane, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<?> addPlane(@RequestBody Airplane plane) {
        try {
            Airplane new_plane = airplaneRepository.save(plane);
            return new ResponseEntity<>(new_plane, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }
}
