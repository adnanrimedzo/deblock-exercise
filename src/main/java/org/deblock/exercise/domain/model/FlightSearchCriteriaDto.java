package org.deblock.exercise.domain.model;

import java.time.LocalDate;

public record FlightSearchCriteriaDto(
        String origin,
        String destination,
        LocalDate departureDate,
        LocalDate returnDate,
        int numberOfPassengers
) {}
