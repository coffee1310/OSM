package com.example.osm.service;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.repository.AirplaneRepository;
import com.example.osm.service.converter.AirplanesConverter;
import com.example.osm.service.converter.ConverterFactory;
import com.example.osm.service.converter.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AirplaneService {
    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    private ConverterFactory converterFactory;

    public Optional<Airplane> findById(Long id) {
        return airplaneRepository.findById(id);
    }

    public Optional<AirplaneDTO> createAirplane(Airplane airplane) {
        EntityConverter<Airplane, AirplaneDTO> converter = new ConverterFactory()
                .getConverter(Airplane.class, AirplaneDTO.class);

        AirplaneDTO airplaneDTO = converter.toDTO(airplane);

        airplaneRepository.save(airplane);
        return Optional.of(airplaneDTO);
    }

    public AirplaneDTO updateAirplane(Airplane airplane, Airplane airplaneDetails) {
        airplane.setModel(airplaneDetails.getModel());
        airplane.setStatus(airplaneDetails.getStatus());
        airplane.setNumber(airplaneDetails.getNumber());


        EntityConverter<Airplane, AirplaneDTO> converter = new ConverterFactory()
                .getConverter(Airplane.class, AirplaneDTO.class);

        AirplaneDTO airplaneDTO = converter.toDTO(airplaneDetails);

        this.save(airplane);
        return airplaneDTO;
    }

    public Airplane save(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public void delete(Airplane airplane) {
        airplaneRepository.delete(airplane);
    }
}
