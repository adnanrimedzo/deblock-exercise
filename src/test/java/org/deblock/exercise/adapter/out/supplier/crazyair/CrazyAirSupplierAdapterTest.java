package org.deblock.exercise.adapter.out.supplier.crazyair;

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

class CrazyAirSupplierAdapterTest {
    private CrazyAirFeignClient feignClient;
    private CrazyAirSupplierAdapter adapter;

    @BeforeEach
    void setUp() {
        feignClient = mock(CrazyAirFeignClient.class);
        adapter = new CrazyAirSupplierAdapter(feignClient);
    }

    @Test
    void shouldReturnMappedFlightsFromFeignClient() {
        final var criteria = new FlightSearchCriteriaDto("LHR", "AMS", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 2), 1);
        final var crazyAirRequest = CrazyAirFlightMapper.INSTANCE.toCrazyAirRequest(criteria);
        final var crazyAirResponse = new CrazyAirResponse("CrazyAir", BigDecimal.valueOf(150), "E", "LHR", "AMS", LocalDateTime.of(2025, 5, 1, 10, 0), LocalDateTime.of(2025, 5, 1, 12, 0));
        when(feignClient.search(crazyAirRequest)).thenReturn(List.of(crazyAirResponse));

        final List<FlightDto> result = adapter.getFlights(criteria);
        assertThat(result).hasSize(1);
        final FlightDto flight = result.get(0);
        assertThat(flight.airline()).isEqualTo("CrazyAir");
        assertThat(flight.fare()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(flight.departureAirportCode()).isEqualTo("LHR");
        assertThat(flight.destinationAirportCode()).isEqualTo("AMS");
        assertThat(flight.departureDate()).isEqualTo(LocalDateTime.of(2025, 5, 1, 10, 0));
        assertThat(flight.arrivalDate()).isEqualTo(LocalDateTime.of(2025, 5, 1, 12, 0));
    }

    @Test
    void shouldReturnEmptyListWhenFeignClientReturnsEmpty() {
        final var criteria = new FlightSearchCriteriaDto("LHR", "AMS", LocalDate.now(), LocalDate.now().plusDays(1), 1);
        final var crazyAirRequest = CrazyAirFlightMapper.INSTANCE.toCrazyAirRequest(criteria);
        when(feignClient.search(crazyAirRequest)).thenReturn(List.of());
        List<FlightDto> result = adapter.getFlights(criteria);
        assertThat(result).isEmpty();
    }
}
