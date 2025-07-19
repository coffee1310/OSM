package com.example.osm.controllers;

import com.example.osm.entity.*;
import com.example.osm.entity.DTO.AirplaneDTO;
import com.example.osm.entity.DTO.FlightDTO;
import com.example.osm.entity.DTO.TicketDTO;
import com.example.osm.entity.DTO.UserDTO;
import com.example.osm.exception.ResourceNotFound;
import com.example.osm.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Epic("Тесты REST контроллера билетов")
@WebMvcTest(TicketController.class)
public class TicketsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET -> /api/tickets/{id} OK, когда билет сущесвует")
    public void getTicketTest_thenStatus400AndTicketDTOReturned() throws Exception {
        Ticket ticket = createTicket(1L);

        when(ticketService.findById(1L)).thenReturn(Optional.of(ticket));

        mockMvc.perform(get("/api/tickets/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.flight").exists())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET -> /api/tickets/{id} Not Found, когда билет не найден")
    public void getTicketTest_thenStatus404() throws Exception {
        Long invalidId = 9999L;

        when(ticketService.findById(invalidId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/flights/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST -> /api/tickets 201 Created, когда билет добавляется ")
    public void addTicketTest_thenStatus201AndTicketDTOReturned() throws Exception {
        Ticket ticket = createTicket(1L);
        TicketDTO ticketDTO = createTicketDTO(1L);

        when(ticketService.createTicket(any(Ticket.class))).thenReturn(Optional.of(ticketDTO));

        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.flight").exists())
                .andExpect(jsonPath("$.user").exists());
    }

    @Test
    @DisplayName("PUT -> /api/tickets/{id} 201 OK, когда билет обновляется")
    public void putTicketTest_thenStatus201AndTicketDTOReturned() throws Exception {
        Ticket ticket = createTicket(1L);
        TicketDTO ticketDTO = createTicketDTO(1L);

        when(ticketService.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketService.updateTicket(any(Ticket.class), any(Ticket.class))).thenReturn(Optional.of(ticketDTO));

        mockMvc.perform(put("/api/tickets/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT -> /api/tickets/{id} 400 Bad Request, когда билет не найден")
    public void putTicketTest_thenStatus400() throws Exception {
        Long invalidId = 1L;
        Ticket ticket = createTicket(invalidId);

        when(ticketService.findById(1L))
                .thenThrow(new ResourceNotFound(String.format("Ticket with id: %d was not found", invalidId)));

        mockMvc.perform(put("/api/tickets/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE -> /api/tickets/{id} OK, когда билет удален")
    public void deleteTicketTest_thenStatus200() throws Exception {
        Ticket ticket = createTicket(1L);

        when(ticketService.findById(1L)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketService).deleteTicket(ticket);

        mockMvc.perform(delete("/api/tickets/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE -> /api/tickets/{id} Not Found, когда билет не найден")
    public void deleteTicketTest_thenStatus404() throws Exception {
        Long invalidId = 9999L;

        when(ticketService.findById(invalidId))
                .thenThrow(new ResourceNotFound(String.format("Ticket with id: %d was not found", invalidId)));

        mockMvc.perform(delete("/api/tickets/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    public Ticket createTicket(Long id) {
        User user = createUser();
        Flight flight = createFlight();

        Ticket ticket = new Ticket();
        ticket.setId(id);
        ticket.setUserId(1L);
        ticket.setFlightId(1L);
        ticket.setUser(user);
        ticket.setFlight(flight);

        return ticket;
    }

    public TicketDTO createTicketDTO(Long id) {
        UserDTO userDTO = createUserDTO();
        FlightDTO flightDTO = createFlightDTO();

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(id);
        ticketDTO.setUser(userDTO);
        ticketDTO.setFlight(flightDTO);

        return ticketDTO;
    }

    public User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Иван Иванович Иванов");
        user.setAge((short) 18);

        return user;
    }

    public UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFullName("Иван Иванович Иванов");
        userDTO.setAge((short) 18);

        return userDTO;
    }

    public Flight createFlight() {
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

    public FlightDTO createFlightDTO() {
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