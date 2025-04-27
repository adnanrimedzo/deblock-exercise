package org.deblock.exercise.adapter.out.supplier.toughjet;

import java.math.BigDecimal;
import java.time.Instant;

public record ToughJetResponse(
        String carrier,
        BigDecimal basePrice,
        BigDecimal tax,
        BigDecimal discount,
        String departureAirportName,
        String arrivalAirportName,
        Instant outboundDateTime,
        Instant inboundDateTime
) {}
