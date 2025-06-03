package com.example.osm.service;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.repository.AirplaneRepository;
import com.example.osm.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirplaneRepository airplaneRepository;

    // Функция для добавления полета, добавляя автоматически самолет
    public Flight createFlight(Flight flight) throws ResourceNotFound{
        Long planeId = flight.getAirplaneId();
        Airplane plane = airplaneRepository.findById(planeId)
                .orElseThrow(() ->
                new ResourceNotFound(String.format("Airplane with id: %d was not found",planeId)));

        flight.setAirplane(plane);
        flightRepository.save(flight);
        return flight;
    }

    // Функция для конвертирования из Flight в FlightDTO для избежания рекурсии
    public FlightDTO convertToFlightDTO(Flight flight) {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(flight.getId());
        flightDTO.setStartPlace(flight.getStartPlace());
        flightDTO.setEndPlace(flight.getEndPlace());
        flightDTO.setStartTime(flight.getStartTime());
        flightDTO.setEndTime(flight.getEndTime());

        if (flight.getAirplane() != null) {
            AirplaneDTO airplaneDTO = new AirplaneDTO();
            airplaneDTO.setId(flight.getAirplane().getId());
            airplaneDTO.setNumber(flight.getAirplane().getNumber());
            airplaneDTO.setModel(flight.getAirplane().getModel());

            flightDTO.setAirplane(airplaneDTO);
        }

        return flightDTO;
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
