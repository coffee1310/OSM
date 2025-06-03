package com.example.osm.entity.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDTO {
    private Long id;
    private String startPlace;
    private String endPlace;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AirplaneDTO airplane;
}
