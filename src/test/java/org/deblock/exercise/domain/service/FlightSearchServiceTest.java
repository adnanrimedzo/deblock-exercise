package org.deblock.exercise.domain.service;

import org.deblock.exercise.application.port.out.SupplierPort;
import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FlightSearchServiceTest {
    private SupplierPort supplier1;
    private SupplierPort supplier2;
    private FlightSearchService service;

    @BeforeEach
    void setUp() {
        supplier1 = mock(SupplierPort.class);
        supplier2 = mock(SupplierPort.class);
        service = new FlightSearchService(List.of(supplier1, supplier2));
    }

    @Test
    void shouldAggregateAndSortFlightsFromAllSuppliers() {
        final var criteria = new FlightSearchCriteriaDto("LHR", "AMS", LocalDate.now(), LocalDate.now().plusDays(1), 1);
        final var flight1 = new FlightDto("A", "S1", BigDecimal.valueOf(200), "LHR", "AMS", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        final var flight2 = new FlightDto("B", "S2", BigDecimal.valueOf(100), "LHR", "AMS", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        when(supplier1.getFlights(criteria)).thenReturn(List.of(flight1));
        when(supplier2.getFlights(criteria)).thenReturn(List.of(flight2));

        final var result = service.searchFlights(criteria);
        assertThat(result).containsExactly(flight2, flight1);
    }

    @Test
    void shouldReturnEmptyListWhenNoSupplierReturnsFlights() {
        final var criteria = new FlightSearchCriteriaDto("LHR", "AMS", LocalDate.now(), LocalDate.now().plusDays(1), 1);
        when(supplier1.getFlights(criteria)).thenReturn(List.of());
        when(supplier2.getFlights(criteria)).thenReturn(List.of());
        final var result = service.searchFlights(criteria);
        assertThat(result).isEmpty();
    }
}
