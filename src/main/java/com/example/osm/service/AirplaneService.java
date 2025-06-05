package com.example.osm.service;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.repository.AirplaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AirplaneService {
    @Autowired
    AirplaneRepository airplaneRepository;

    public Optional<Airplane> findById(Long id) {
        return airplaneRepository.findById(id);
    }

    public AirplaneDTO createAirplane(Airplane airplane) {
        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(airplane.getId());
        airplaneDTO.setModel(airplane.getModel());
        airplaneDTO.setNumber(airplane.getNumber());

        airplaneRepository.save(airplane);
        return airplaneDTO;
    }

    public AirplaneDTO updateAirplane(Airplane airplane, Airplane airplaneDetails) {
        airplane.setModel(airplaneDetails.getModel());
        airplane.setStatus(airplaneDetails.getStatus());
        airplane.setNumber(airplaneDetails.getNumber());

        airplaneRepository.save(airplane);

        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(airplaneDetails.getId());
        airplaneDTO.setModel(airplaneDetails.getModel());
        airplaneDTO.setNumber(airplaneDetails.getNumber());
        return airplaneDTO;
    }

    public AirplaneDTO convertToAirplaneDTO(Airplane airplane) {
        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(airplane.getId());
        airplaneDTO.setNumber(airplane.getNumber());
        airplane.setModel(airplaneDTO.getModel());
        airplane.setStatus(airplane.getStatus());

        return airplaneDTO;
    }
}
