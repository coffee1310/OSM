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


    public AirplaneDTO getById(Long id) {
        return convertToAirplaneDTO(airplaneRepository.findById(id).get());
    }

    public AirplaneDTO createAirplane(Airplane airplane) {
        airplane = airplaneRepository.save(airplane);

        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(airplane.getId());
        airplaneDTO.setModel(airplane.getModel());
        airplaneDTO.setNumber(airplane.getNumber());
        airplaneDTO.validate();

        return airplaneDTO;
    }

    public AirplaneDTO updateAirplane(Airplane airplane, Airplane airplaneDetails) {
        airplane.setModel(airplaneDetails.getModel());
        airplane.setStatus(airplaneDetails.getStatus());
        airplane.setNumber(airplaneDetails.getNumber());

        airplaneRepository.save(airplane);

        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(airplane.getId());
        airplaneDTO.setModel(airplaneDetails.getModel());
        airplaneDTO.setNumber(airplaneDetails.getNumber());
        airplaneDTO.validate();

        return airplaneDTO;
    }

    public Airplane save(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public void delete(Airplane airplane) {
        airplaneRepository.delete(airplane);
    }

    public AirplaneDTO convertToAirplaneDTO(Airplane airplane) {
        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(airplane.getId());
        airplaneDTO.setNumber(airplane.getNumber());
        airplaneDTO.setModel(airplane.getModel());
        airplaneDTO.setStatus(airplane.getStatus());
        airplaneDTO.validate();

        return airplaneDTO;
    }
}
