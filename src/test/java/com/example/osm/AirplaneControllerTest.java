package com.example.osm;

import com.example.osm.controllers.AirplanesController;
import com.example.osm.entity.Airplane;
import com.example.osm.entity.AirplaneStatus;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.service.AirplaneService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
}