package com.example.osm.service;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.repository.AirplaneRepository;
import com.example.osm.repository.FlightRepository;
import com.example.osm.service.DTOConverter.DTOConverterFactory;
import com.example.osm.service.DTOConverter.EntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;
    private final DTOConverterFactory converterFactory;

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository, DTOConverterFactory converterFactory,
                         AirplaneRepository airplaneRepository) {
        this.flightRepository = flightRepository;
        this.converterFactory = converterFactory;
        this.airplaneRepository = airplaneRepository;
    }

    // Функция для добавления полета, добавляя автоматически самолет
    public Optional<FlightDTO> createFlight(Flight flight) throws ResourceNotFound {
        Long planeId = flight.getAirplaneId();
        Airplane plane = airplaneRepository.findById(planeId)
                .orElseThrow(() ->
                new ResourceNotFound(String.format("Airplane with id: %d was not found",planeId)));

        flight.setAirplane(plane);
        flightRepository.save(flight);

        EntityConverter<Flight, FlightDTO> converter = converterFactory
                .getConverter(Flight.class, FlightDTO.class);

        FlightDTO flightDTO = converter.toDTO(flight);
        return Optional.of(flightDTO);
    }

    public Optional<FlightDTO> putFlight(Flight flight, Flight flightDetails) {
        flight.setStartPlace(flightDetails.getStartPlace());
        flight.setEndPlace(flightDetails.getEndPlace());
        flight.setStartTime(flightDetails.getStartTime());
        flight.setEndTime(flightDetails.getEndTime());
        flightRepository.save(flight);

        EntityConverter<Flight, FlightDTO> converter = converterFactory
                .getConverter(Flight.class, FlightDTO.class);

        FlightDTO flightDTO = converter.toDTO(flightDetails);
        return Optional.of(flightDTO);
    }

    public Optional<Flight> findById(Long id) {
        return flightRepository.findById(id);
    }

    public Flight save(Flight flight) {
        if (flight.getAirplane() == null && flight.getAirplaneId() != null) {
            flight.setAirplane(airplaneRepository.findById(flight.getAirplaneId()).get());
        }
        return flightRepository.save(flight);
    }

    public void delete(Flight flight) {
        flightRepository.delete(flight);
    }
}
