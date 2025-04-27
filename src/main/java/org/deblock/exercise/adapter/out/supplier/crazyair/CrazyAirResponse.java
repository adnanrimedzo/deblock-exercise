package org.deblock.exercise.adapter.out.supplier.crazyair;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CrazyAirResponse(
        String airline,
        BigDecimal price,
        String cabinclass,
        String departureAirportCode,
        String destinationAirportCode,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate
) {}
