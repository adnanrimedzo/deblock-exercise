package org.deblock.exercise.adapter.out.supplier.crazyair;

import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CrazyAirFlightMapperTest {
    @Test
    void shouldMapFlightSearchCriteriaDtoToCrazyAirRequest() {
        final var criteria = new FlightSearchCriteriaDto("LHR", "AMS", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 2), 3);
        final var req = CrazyAirFlightMapper.INSTANCE.toCrazyAirRequest(criteria);
        assertThat(req.origin()).isEqualTo("LHR");
        assertThat(req.destination()).isEqualTo("AMS");
        assertThat(req.departureDate()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(req.returnDate()).isEqualTo(LocalDate.of(2025, 5, 2));
        assertThat(req.passengerCount()).isEqualTo(3);
    }

    @Test
    void shouldMapCrazyAirResponseToFlightDto() {
        final var response = new CrazyAirResponse(
                "CrazyAir",
                BigDecimal.valueOf(150),
                "E",
                "LHR",
                "AMS",
                LocalDateTime.of(2025, 5, 1, 10, 0),
                LocalDateTime.of(2025, 5, 1, 12, 0)
        );
        final FlightDto dto = CrazyAirFlightMapper.INSTANCE.toFlight(response);
        assertThat(dto.airline()).isEqualTo("CrazyAir");
        assertThat(dto.fare()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(dto.departureAirportCode()).isEqualTo("LHR");
        assertThat(dto.destinationAirportCode()).isEqualTo("AMS");
        assertThat(dto.departureDate()).isEqualTo(LocalDateTime.of(2025, 5, 1, 10, 0));
        assertThat(dto.arrivalDate()).isEqualTo(LocalDateTime.of(2025, 5, 1, 12, 0));
    }
}
