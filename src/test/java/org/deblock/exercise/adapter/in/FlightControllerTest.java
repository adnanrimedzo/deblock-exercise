package org.deblock.exercise.adapter.in;

import org.deblock.exercise.application.port.in.FlightSearchUseCase;
import org.deblock.exercise.domain.model.FlightDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FlightControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightSearchUseCase flightSearchUseCase;

    @Test
    void shouldReturnFlightsFromSuppliers() throws Exception {
        final FlightDto flight1 = new FlightDto("CA", "CrazyAir", BigDecimal.valueOf(100), "LHR", "AMS", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        final FlightDto flight2 = new FlightDto("TJ", "ToughJet", BigDecimal.valueOf(90), "LHR", "AMS", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        Mockito.when(flightSearchUseCase.searchFlights(any())).thenReturn(List.of(flight1, flight2));

        mockMvc.perform(get("/flights/search")
                        .param("origin", "LHR")
                        .param("destination", "AMS")
                        .param("departureDate", LocalDate.now().plusDays(1).toString())
                        .param("returnDate", LocalDate.now().plusDays(2).toString())
                        .param("numberOfPassengers", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].airline").value("CA"))
                .andExpect(jsonPath("$[1].airline").value("TJ"));
    }

    @Test
    void shouldReturnEmptyListWhenNoFlights() throws Exception {
        Mockito.when(flightSearchUseCase.searchFlights(any())).thenReturn(List.of());

        mockMvc.perform(get("/flights/search")
                        .param("origin", "LHR")
                        .param("destination", "AMS")
                        .param("departureDate", LocalDate.now().plusDays(1).toString())
                        .param("returnDate", LocalDate.now().plusDays(2).toString())
                        .param("numberOfPassengers", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnBadRequestForInvalidOrigin() throws Exception {
        mockMvc.perform(get("/flights/search")
                        .param("origin", "L") // Invalid
                        .param("destination", "AMS")
                        .param("departureDate", LocalDate.now().plusDays(1).toString())
                        .param("returnDate", LocalDate.now().plusDays(2).toString())
                        .param("numberOfPassengers", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForInvalidNumberOfPassengers() throws Exception {
        mockMvc.perform(get("/flights/search")
                        .param("origin", "LHR")
                        .param("destination", "AMS")
                        .param("departureDate", LocalDate.now().plusDays(1).toString())
                        .param("returnDate", LocalDate.now().plusDays(2).toString())
                        .param("numberOfPassengers", "0") // Invalid
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalServerErrorOnException() throws Exception {
        Mockito.when(flightSearchUseCase.searchFlights(any())).thenThrow(new RuntimeException("Supplier error"));

        mockMvc.perform(get("/flights/search")
                        .param("origin", "LHR")
                        .param("destination", "AMS")
                        .param("departureDate", LocalDate.now().plusDays(1).toString())
                        .param("returnDate", LocalDate.now().plusDays(2).toString())
                        .param("numberOfPassengers", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
