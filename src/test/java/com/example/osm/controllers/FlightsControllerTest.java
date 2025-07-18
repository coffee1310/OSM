package com.example.osm.controllers;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.AirplaneStatus;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.Flight;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.AirplaneService;
import com.example.osm.service.FlightService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        Flight flight = createFlight(requestId);

        when(flightService.findById(requestId)).thenReturn(Optional.of(flight));

        mockMvc.perform(get("/api/flights/{id}", requestId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.startPlace").value("Tokyo"))
                .andExpect(jsonPath("$.endPlace").value("Moscow"));
    }

    @Test
    @DisplayName("GET /api/airplanes/{id} -> 404 NOT_FOUND, когда полет не существует")
    public void get_FlightTest_thenStatus404() throws Exception{
        Long nonExistId = 9999L;

        when(airplaneService.findById(nonExistId)).thenReturn(null);

        mockMvc.perform(get("/api/airplanes/{id}", nonExistId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/airplanes -> 201 OK, когда полет создан")
    public void addFlightTest_thenStatus201AndFlightDTOReturned() throws Exception {
        Long requestId = 1L;
        Flight flight = createFlight(requestId);
        FlightDTO expectedDTO = createFlightDTO(requestId);

        when(flightService.createFlight(any(Flight.class))).thenReturn(Optional.of(expectedDTO));

        mockMvc.perform(post("/api/flights")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.startPlace").value("Tokyo"))
                .andExpect(jsonPath("$.endPlace").value("Moscow"));
    }

    @Test
    @DisplayName("PUT /api/flights/{id} -> 200 OK, когда полет обновляется")
    public void putFlightTest_thenStatus200AndFlightDTOReturned() throws Exception {
        Flight requestFlight = createFlight(1L);
        Flight flightDetails = createFlight(1L);

        FlightDTO responseDTO = createFlightDTO(1L);

        when(flightService.findById(requestFlight.getId())).thenReturn(Optional.of(requestFlight));
        when(flightService.putFlight(any(Flight.class), any(Flight.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/flights/{id}", 1L)
                        .content(objectMapper.writeValueAsString(flightDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/flights/{id} -> 400 Bad Request, когда не найден полет для обновления")
    public void putFlightTest_thenStatus400AndResourceNotFoundReturned() throws Exception {
        Long invalidId = 9999L;

        Flight flight = createFlight(invalidId);

        when(flightService.findById(invalidId)).thenThrow(
                new ResourceNotFound(String.format("Flight with id: %d was not found", invalidId)));

        mockMvc.perform(put("/api/flights/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/flights/{id} -> 200 OK, когда полет удаляется")
    public void deleteFlightTest_thenStatus201() throws Exception {
        Long flightId = 1L;

        Flight flight = createFlight(flightId);

        when(flightService.findById(flightId)).thenReturn(Optional.of(flight));
        doNothing().when(flightService).delete(flight);

        mockMvc.perform(delete("/api/flights/{id}", flightId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/flights/{id} -> 400 Bad Request, когда полет не найден")
    public void deleteFlightTest_thenStatus404AndResourceNotFound() throws Exception {
        Long flightId = 1L;

        Flight flight = createFlight(flightId);

        when(flightService.findById(flightId))
                .thenThrow(new ResourceNotFound(String.format("Airplane with id: %d was not found", flightId)));

        mockMvc.perform(delete("/api/flights/{id}", flightId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    public Flight createFlight(Long id) {
        LocalDateTime startTime = LocalDateTime.of(2025, 7, 17, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 17, 20, 0);
        Airplane airplaneDetails = createAirplane(1L);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setAirplane(airplaneDetails);
        flight.setAirplaneId(1L);
        flight.setStartTime(startTime);
        flight.setEndTime(endTime);
        flight.setStartPlace("Tokyo");
        flight.setEndPlace("Moscow");

        return flight;
    }

    public FlightDTO createFlightDTO(Long id) {
        AirplaneDTO airplaneDTO = createAirplaneDTO(1L);

        LocalDateTime startTime = LocalDateTime.of(2025, 7, 17, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 7, 17, 20, 0);

        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(1L);
        flightDTO.setAirplane(airplaneDTO);
        flightDTO.setStartTime(startTime);
        flightDTO.setEndTime(endTime);
        flightDTO.setStartPlace("Tokyo");
        flightDTO.setEndPlace("Moscow");

        return flightDTO;
    }

    public Airplane createAirplane(Long id) {
        Airplane airplane = new Airplane();
        airplane.setId(id);
        airplane.setModel("Boeing 737");
        airplane.setNumber("ABC123");
        airplane.setStatus(AirplaneStatus.IN_SERVICE);

        return airplane;
    }

    public AirplaneDTO createAirplaneDTO(Long id) {
        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(1L);
        airplaneDTO.setModel("Boeing 737");
        airplaneDTO.setNumber("ABC123");
        airplaneDTO.setStatus(AirplaneStatus.LANDED);

        return airplaneDTO;
    }
}
