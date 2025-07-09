package com.example.osm.service.converter;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.DTO.AirplaneDTO;

public class AirplanesConverter implements EntityConverter<Airplane, AirplaneDTO> {
    @Override
    public Airplane toEntity(AirplaneDTO DTO) {
        DTO.validate();
        
        Airplane airplane = new Airplane();
        airplane.setId(DTO.getId());
        airplane.setModel(DTO.getModel());
        airplane.setNumber(DTO.getNumber());
        airplane.setStatus(DTO.getStatus());
        return airplane;
    }

    @Override
    public AirplaneDTO toDTO(Airplane entity) {
        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(entity.getId());
        airplaneDTO.setNumber(entity.getNumber());
        airplaneDTO.setModel(entity.getModel());
        airplaneDTO.setStatus(entity.getStatus());
        airplaneDTO.validate();

        return  airplaneDTO;
    }
}
