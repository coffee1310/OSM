package com.example.osm.controllers;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.AirplaneStatus;
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
        when(flightService.convertToFlightDTO(flight)).thenReturn(expectedDTO);

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

        when(flightService.createFlight(any(Flight.class))).thenReturn(expectedDTO);

        mockMvc.perform(post("/api/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.startPlace").value("Moscow"))
                .andExpect(jsonPath("$.endPlace").value("Tokyo"));
    }


}
