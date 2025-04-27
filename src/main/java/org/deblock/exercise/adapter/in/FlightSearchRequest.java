package org.deblock.exercise.adapter.in;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record FlightSearchRequest(
        @NotBlank @Pattern(regexp = "[A-Z]{3}", message = "Invalid origin airport code") String origin,
        @NotBlank @Pattern(regexp = "[A-Z]{3}", message = "Invalid destination airport code") String destination,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
        @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
        @NotNull @Min(value = 1, message = "Number of passengers must be at least 1") @Max(value = 4, message = "Number of passengers must be at most 4") Integer numberOfPassengers
) {}
