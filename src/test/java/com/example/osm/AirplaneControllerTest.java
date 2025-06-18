package com.example.osm;

import com.example.osm.controllers.AirplanesController;
import com.example.osm.entity.Airplane;
import com.example.osm.entity.AirplaneStatus;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.AirplaneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
class AirplaneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AirplaneService airplaneService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void getAirPlaneTest_thenStatus200andAirplaneDTOReturned() throws Exception {
        Airplane airplane = new Airplane();

        Long airplaneID = 1L;
        airplane.setId(airplaneID);
        airplane.setModel("Boeing 737");
        airplane.setNumber("ABC123");
        airplane.setStatus(AirplaneStatus.IN_SERVICE);

        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(airplaneID);
        airplaneDTO.setModel(airplane.getModel());
        airplaneDTO.setNumber(airplane.getNumber());
        airplaneDTO.setStatus(airplane.getStatus());

        when(airplaneService.findById(airplaneID)).thenReturn(Optional.of(airplane));
        when(airplaneService.convertToAirplaneDTO(airplane)).thenReturn(airplaneDTO);

        mockMvc.perform(
                get("/api/airplanes/{id}", airplaneID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.model").value("Boeing 737"))
                .andExpect(jsonPath("$.number").value("ABC123"))
                .andExpect(jsonPath("$.status").value(AirplaneStatus.IN_SERVICE.toString()));
    }

    @Test
    public void getAirplaneTest_thenStatus404() throws Exception {
        Long nonExistId = 9999L;

        when(airplaneService.getById(nonExistId)).thenReturn(null);

        mockMvc.perform(
                get("/api/airplanes/{id}", nonExistId))
                .andExpect(status().isNotFound());
    }

    @Test
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
                .thenReturn(responseDTO);

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
    public void addAirplaneTest_thenStatus400() throws Exception {
        Airplane invalidAirplane = new Airplane();
        when(airplaneService.createAirplane(any(Airplane.class)))
                .thenThrow(new ValidationException("Invalid data"));

        mockMvc.perform(post("/api/airplanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAirplane)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
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
    public void deleteAirplaneTest_thenStatus400AndResourceNotFound() throws Exception{
        Long invalidId = 9999L;

        when(airplaneService.findById(invalidId))
                .thenThrow(new ResourceNotFound(String.format("Airplane with id: %d was not found", invalidId)));


        mockMvc.perform(delete("/api/airplanes/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}