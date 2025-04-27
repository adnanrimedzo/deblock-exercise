package org.deblock.exercise.adapter.in;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightSearchResponse(
        String airline,
        String supplier,
        BigDecimal fare,
        String departureAirportCode,
        String destinationAirportCode,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate
) {}
