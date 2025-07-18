package com.example.osm.controllers;

import com.example.osm.entity.Airplane;
import com.example.osm.entity.AirplaneStatus;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.AirplaneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.qameta.allure.*;

@Epic("Тесты REST контроллера самолетов")
@WebMvcTest(AirplanesController.class)
class AirplanesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AirplaneService airplaneService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/airplanes/{id} -> 200 OK, когда самолет существует")
    public void getAirPlaneTest_thenStatus200andAirplaneDTOReturned() throws Exception {
        Airplane airplane = new Airplane();

        Long airplaneID = 1L;
        airplane.setId(airplaneID);
        airplane.setModel("Boeing 737");
        airplane.setNumber("ABC123");
        airplane.setStatus(AirplaneStatus.IN_SERVICE);

        when(airplaneService.findById(airplaneID)).thenReturn(Optional.of(airplane));

        mockMvc.perform(
                get("/api/airplanes/{id}", airplaneID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.model").value("Boeing 737"))
                .andExpect(jsonPath("$.number").value("ABC123"))
                .andExpect(jsonPath("$.status").value(AirplaneStatus.IN_SERVICE.toString()));
    }

    @Test
    @DisplayName("GET /api/airplanes/{id} -> Not Found 404, когда самолет не существует")
    public void getAirplaneTest_thenStatus404() throws Exception {
        Long nonExistId = 9999L;

        when(airplaneService.findById(nonExistId)).thenReturn(Optional.empty());

        mockMvc.perform(
                get("/api/airplanes/{id}", nonExistId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/airplanes -> 201 Created, когда самолет добавлен")
    public void addAirplaneTest_thenStatus201AndAirplaneDTOReturned() throws Exception {
        Airplane requestAirplane = new Airplane();
        requestAirplane.setModel("Boeing 737");
        requestAirplane.setNumber("ABC123");
        requestAirplane.setStatus(AirplaneStatus.IN_SERVICE);

        AirplaneDTO responseDTO = new AirplaneDTO();
        responseDTO.setId(1L);
        responseDTO.setModel("Boeing 737");
        responseDTO.setNumber("ABC123");
        responseDTO.setStatus(AirplaneStatus.IN_SERVICE);

        when(airplaneService.createAirplane(any(Airplane.class)))
                .thenReturn(Optional.of(responseDTO));

        mockMvc.perform(post("/api/airplanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAirplane)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.model").value("Boeing 737"))
                .andExpect(jsonPath("$.number").value("ABC123"))
                .andExpect(jsonPath("$.status").value(AirplaneStatus.IN_SERVICE.toString()));
    }

    @Test
    @DisplayName("PUT /api/airplanes/{id} -> 201 OK, когда самолет обновляется")
    public void putAirplaneTest_thenStatus201AndAirplaneDTOReturned() throws Exception{
        Airplane requestAirplane = new Airplane();
        requestAirplane.setId(1L);
        requestAirplane.setModel("Boeing 737");
        requestAirplane.setNumber("ABC123");
        requestAirplane.setStatus(AirplaneStatus.IN_SERVICE);

        Airplane airplaneDetails = new Airplane();
        airplaneDetails.setId(1L);
        airplaneDetails.setModel("Boeing 757");
        airplaneDetails.setNumber("CBA123");
        airplaneDetails.setStatus(AirplaneStatus.LANDED);

        AirplaneDTO responseDTO = new AirplaneDTO();
        responseDTO.setId(1L);
        responseDTO.setModel("Boeing 757");
        responseDTO.setNumber("CBA123");
        responseDTO.setStatus(AirplaneStatus.LANDED);

        when(airplaneService.findById(requestAirplane.getId())).thenReturn(Optional.of(requestAirplane));
        when(airplaneService.updateAirplane(requestAirplane, airplaneDetails)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/airplanes/{id}", 1L)
                .content(objectMapper.writeValueAsString(responseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/airplanes/{id} -> 400 Bad Request, когда не найден самолет для обновления")
    public void putAirplaneTest_thenStatus400AndResourceNotFound() throws Exception {
        Long invalidId = 9999L;

        Airplane airplane = new Airplane();
        airplane.setModel("Boeing 737");
        airplane.setNumber("ABC123");
        airplane.setStatus(AirplaneStatus.IN_SERVICE);

        when(airplaneService.findById(invalidId))
                .thenThrow(new ResourceNotFound(String.format("Airplane with id: %d was not found", invalidId)));

        mockMvc.perform(put("/api/airplanes/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(airplane)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/airplanes/{id} -> 201 OK, когда самолет существует")
    public void deleteAirplaneTest_thenStatus201() throws Exception {
        Long airplaneId = 1L;

        Airplane airplane = new Airplane();
        airplane.setId(airplaneId);
        airplane.setModel("Boeing 737");
        airplane.setNumber("ABC123");
        airplane.setStatus(AirplaneStatus.IN_SERVICE);

        when(airplaneService.findById(airplaneId)).thenReturn(Optional.of(airplane));
        doNothing().when(airplaneService).delete(airplane);

        mockMvc.perform(delete("/api/airplanes/{id}", airplaneId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/airplanes/{id} -> 400 Bad Request, когда самолет не найден")
    public void deleteAirplaneTest_thenStatus400AndResourceNotFound() throws Exception{
        Long invalidId = 9999L;

        when(airplaneService.findById(invalidId))
                .thenThrow(new ResourceNotFound(String.format("Airplane with id: %d was not found", invalidId)));


        mockMvc.perform(delete("/api/airplanes/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}