package com.example.osm.service.converter;

import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;

public class FlightsConverter implements EntityConverter<Flight, FlightDTO> {
    @Override
    public Flight toEntity(FlightDTO DTO) {
        DTO.validate();

        Flight flight = new Flight();
        flight.setId(DTO.getId());
        flight.setStartPlace(DTO.getStartPlace());
        flight.setEndPlace(DTO.getEndPlace());
        flight.setStartTime(DTO.getStartTime());
        flight.setEndTime(DTO.getEndTime());
        flight.setAirplaneId(DTO.getAirplane().getId());
        return flight;
    }

    @Override
    public FlightDTO toDTO(Flight entity) {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(entity.getId());
        flightDTO.setStartPlace(entity.getStartPlace());
        flightDTO.setEndPlace(entity.getEndPlace());
        flightDTO.setStartTime(entity.getStartTime());
        flightDTO.setEndTime(entity.getEndTime());

        if (entity.getAirplane() != null) {
            AirplanesConverter airplanesConverter = new AirplanesConverter();
            AirplaneDTO airplaneDTO = airplanesConverter.toDTO(entity.getAirplane());
            flightDTO.setAirplane(airplaneDTO);
        }

        flightDTO.validate();
        return flightDTO;
    }
}
