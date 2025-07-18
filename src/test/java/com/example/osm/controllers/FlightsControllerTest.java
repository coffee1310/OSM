package com.example.osm.controllers;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.AirplaneStatus;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.service.AirplaneService;
import com.example.osm.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Epic;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Epic("Тестирование FlightController")
@WebMvcTest(FlightsController.class)
class FlightsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlightService flightService;

    @MockitoBean
    private AirplaneService airplaneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/flights/{id} -> 200 OK, когда полет существует")
    public void get_FlightTest_thenStatus200AndFlightDTOReturned() throws Exception{
        Long requestId = 1L;
        Flight flight = new Flight();
        flight.setId(requestId);
        flight.setStartPlace("Moscow");
        flight.setEndPlace("Tokyo");
        flight.setStartTime(LocalDateTime.now());
        flight.setEndTime(LocalDateTime.now().plusHours(10));

        Airplane airplane = new Airplane();
        airplane.setId(1L);
        flight.setAirplane(airplane);

        FlightDTO expectedDTO = new FlightDTO();
        expectedDTO.setId(1L);
        expectedDTO.setStartPlace("Moscow");
        expectedDTO.setEndPlace("Tokyo");
        expectedDTO.setStartTime(flight.getStartTime());
        expectedDTO.setEndTime(flight.getEndTime());

        when(flightService.findById(requestId)).thenReturn(Optional.of(flight));

        mockMvc.perform(get("/api/flights/{id}", requestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.startPlace").value("Moscow"))
                .andExpect(jsonPath("$.endPlace").value("Tokyo"));
    }

    @Test
    @DisplayName("GET /api/airplanes/{id} -> 404 NOT_FOUND, когда полет не существует")
    public void get_FlightTest_thenStatus404() throws Exception{
        Long nonExistId = 9999L;

        when(airplaneService.findById(nonExistId)).thenReturn(null);

        mockMvc.perform(get("/api/airplanes/{id}", 9999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/airplanes -> 201 OK, когда полет создан")
    public void addFlightTest_thenStatus201AndFlightDTOReturned() throws Exception {
        Long requestId = 1L;
        Flight flight = new Flight();
        flight.setId(requestId);
        flight.setStartPlace("Moscow");
        flight.setEndPlace("Tokyo");
        flight.setStartTime(LocalDateTime.now());
        flight.setEndTime(LocalDateTime.now().plusHours(10));
        flight.setAirplaneId(1L);

        Airplane airplane = new Airplane();
        airplane.setId(1L);
        flight.setAirplane(airplane);

        FlightDTO expectedDTO = new FlightDTO();
        expectedDTO.setId(1L);
        expectedDTO.setStartPlace("Moscow");
        expectedDTO.setEndPlace("Tokyo");
        expectedDTO.setStartTime(flight.getStartTime());
        expectedDTO.setEndTime(flight.getEndTime());

        when(flightService.createFlight(any(Flight.class))).thenReturn(Optional.of(expectedDTO));

        mockMvc.perform(post("/api/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.startPlace").value("Moscow"))
                .andExpect(jsonPath("$.endPlace").value("Tokyo"));
    }

    @Test
    @DisplayName("PUT /api/flights/{id} -> 200 OK, когда полет обновляется")
    public void putAirplaneTest_thenStatus200AndAirplaneDTOReturned() throws Exception {
        // Подготовка данных
        Airplane airplaneDetails = new Airplane();
        airplaneDetails.setId(1L);
        airplaneDetails.setModel("Boeing 757");
        airplaneDetails.setNumber("CBA123");
        airplaneDetails.setStatus(AirplaneStatus.LANDED);

        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(1L);
        airplaneDTO.setModel("Boeing 757");
        airplaneDTO.setNumber("CBA123");
        airplaneDTO.setStatus(AirplaneStatus.LANDED);

        // Фиксированные даты для теста
        LocalDateTime startTime = LocalDateTime.of(2025, 7, 17, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 17, 20, 0);

        Flight requestFlight = new Flight();
        requestFlight.setId(1L);
        requestFlight.setAirplane(airplaneDetails);
        requestFlight.setAirplaneId(1L);
        requestFlight.setStartTime(startTime);
        requestFlight.setEndTime(endTime);
        requestFlight.setStartPlace("Moscow");
        requestFlight.setEndPlace("Tokyo");

        Flight flightDetails = new Flight();
        flightDetails.setId(1L);
        flightDetails.setAirplane(airplaneDetails);
        flightDetails.setAirplaneId(1L);
        flightDetails.setStartTime(startTime);
        flightDetails.setEndTime(endTime);
        flightDetails.setStartPlace("Tokyo");
        flightDetails.setEndPlace("Moscow");

        FlightDTO responseDTO = new FlightDTO();
        responseDTO.setId(1L);
        responseDTO.setAirplane(airplaneDTO);
        responseDTO.setStartTime(startTime);
        responseDTO.setEndTime(endTime);
        responseDTO.setStartPlace("Tokyo");
        responseDTO.setEndPlace("Moscow");

        when(flightService.findById(requestFlight.getId())).thenReturn(Optional.of(requestFlight));
        when(flightService.putFlight(any(Flight.class), any(Flight.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/flights/{id}", 1L)
                        .content(objectMapper.writeValueAsString(flightDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
