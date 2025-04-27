package org.deblock.exercise.adapter.out.supplier.toughjet;

import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ToughJetFlightMapperTest {
    @Test
    void shouldMapFlightSearchCriteriaDtoToToughJetRequest() {
        final var criteria = new FlightSearchCriteriaDto("LHR", "AMS", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 2), 2);
        final var req = ToughJetFlightMapper.INSTANCE.toToughJetRequest(criteria);
        assertThat(req.from()).isEqualTo("LHR");
        assertThat(req.to()).isEqualTo("AMS");
        assertThat(req.outboundDate()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(req.inboundDate()).isEqualTo(LocalDate.of(2025, 5, 2));
        assertThat(req.numberOfAdults()).isEqualTo(2);
    }

    @Test
    void shouldMapToughJetResponseToFlightDtoAndCalculateFare() {
        final var response = new ToughJetResponse(
                "ToughJet",
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(5),
                "LHR",
                "AMS",
                Instant.parse("2025-05-01T10:00:00Z"),
                Instant.parse("2025-05-01T12:00:00Z")
        );
        final FlightDto dto = ToughJetFlightMapper.INSTANCE.toFlight(response);
        assertThat(dto.airline()).isEqualTo("ToughJet");
        assertThat(dto.fare()).isEqualByComparingTo(BigDecimal.valueOf(114.0)); // (100+20)*(1-0.05)
        assertThat(dto.departureAirportCode()).isEqualTo("LHR");
        assertThat(dto.destinationAirportCode()).isEqualTo("AMS");
        assertThat(dto.departureDate()).isEqualTo(LocalDateTime.of(2025, 5, 1, 10, 0));
        assertThat(dto.arrivalDate()).isEqualTo(LocalDateTime.of(2025, 5, 1, 12, 0));
    }
}
