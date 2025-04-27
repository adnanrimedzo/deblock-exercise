package org.deblock.exercise.adapter.out.supplier.crazyair;

import java.time.LocalDate;

public record CrazyAirRequest(
        String origin,
        String destination,
        LocalDate departureDate,
        LocalDate returnDate,
        int passengerCount
) {}
