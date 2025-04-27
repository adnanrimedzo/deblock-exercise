package org.deblock.exercise.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightDto(
        String airline,
        String supplier,
        BigDecimal fare,
        String departureAirportCode,
        String destinationAirportCode,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate
) implements Comparable<FlightDto> {
    @Override
    public int compareTo(final FlightDto other) {
        return this.fare.compareTo(other.fare);
    }
}
