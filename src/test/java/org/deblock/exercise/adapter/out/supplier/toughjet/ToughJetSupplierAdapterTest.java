package org.deblock.exercise.adapter.out.supplier.toughjet;

import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ToughJetSupplierAdapterTest {
    private ToughJetFeignClient feignClient;
    private ToughJetSupplierAdapter adapter;

    @BeforeEach
    void setUp() {
        feignClient = mock(ToughJetFeignClient.class);
        adapter = new ToughJetSupplierAdapter(feignClient);
    }

    @Test
    void shouldReturnMappedFlightsFromFeignClient() {
        final var criteria = new FlightSearchCriteriaDto("LHR", "AMS", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 2), 1);
        final var toughJetRequest = ToughJetFlightMapper.INSTANCE.toToughJetRequest(criteria);
        final var toughJetResponse = new ToughJetResponse(
                "ToughJet",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(5),
                "LHR",
                "AMS",
                Instant.parse("2025-05-01T10:00:00Z"),
                Instant.parse("2025-05-01T12:00:00Z")
        );
        when(feignClient.search(toughJetRequest)).thenReturn(List.of(toughJetResponse));

        final List<FlightDto> result = adapter.getFlights(criteria);
        assertThat(result).hasSize(1);
        final FlightDto flight = result.get(0);
        assertThat(flight.airline()).isEqualTo("ToughJet");
        // Fare calculation: (100 + 20) * (1 - 0.05) = 114.0
        assertThat(flight.fare()).isEqualByComparingTo(BigDecimal.valueOf(114.0));
        assertThat(flight.departureAirportCode()).isEqualTo("LHR");
        assertThat(flight.destinationAirportCode()).isEqualTo("AMS");
        assertThat(flight.departureDate()).isEqualTo(LocalDateTime.of(2025, 5, 1, 10, 0));
        assertThat(flight.arrivalDate()).isEqualTo(LocalDateTime.of(2025, 5, 1, 12, 0));
    }

    @Test
    void shouldReturnEmptyListWhenFeignClientReturnsEmpty() {
        final var criteria = new FlightSearchCriteriaDto("LHR", "AMS", LocalDate.now(), LocalDate.now().plusDays(1), 1);
        final var toughJetRequest = ToughJetFlightMapper.INSTANCE.toToughJetRequest(criteria);
        when(feignClient.search(toughJetRequest)).thenReturn(List.of());
        List<FlightDto> result = adapter.getFlights(criteria);
        assertThat(result).isEmpty();
    }
}
