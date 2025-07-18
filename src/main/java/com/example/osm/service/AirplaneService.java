package com.example.osm.service;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.repository.AirplaneRepository;
import com.example.osm.service.DTOConverter.DTOConverterFactory;
import com.example.osm.service.DTOConverter.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AirplaneService {
    @Autowired
    private final AirplaneRepository airplaneRepository;

    @Autowired
    private final DTOConverterFactory converterFactory;

    @Autowired
    public AirplaneService(AirplaneRepository airplaneRepository,
                           EntityConverter<Airplane, AirplaneDTO> converter, DTOConverterFactory converterFactory) {
        this.airplaneRepository = airplaneRepository;
        this.converterFactory = converterFactory;
    }

    public Optional<Airplane> findById(Long id) {
        return airplaneRepository.findById(id);
    }

    public Optional<AirplaneDTO> createAirplane(Airplane airplane) {
        EntityConverter<Airplane, AirplaneDTO> converter =
                converterFactory.getConverter(Airplane.class, AirplaneDTO.class);
        AirplaneDTO airplaneDTO = converter.toDTO(airplane);

        airplaneRepository.save(airplane);
        return Optional.of(airplaneDTO);
    }

    public AirplaneDTO updateAirplane(Airplane airplane, Airplane airplaneDetails) {
        airplane.setModel(airplaneDetails.getModel());
        airplane.setStatus(airplaneDetails.getStatus());
        airplane.setNumber(airplaneDetails.getNumber());

        EntityConverter<Airplane, AirplaneDTO> converter =
                converterFactory.getConverter(Airplane.class, AirplaneDTO.class);
        AirplaneDTO airplaneDTO = converter.toDTO(airplaneDetails);

        airplaneRepository.save(airplane);
        return airplaneDTO;
    }

    public void delete(Airplane airplane) {
        airplaneRepository.delete(airplane);
    }
}