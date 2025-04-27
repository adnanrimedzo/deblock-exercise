package org.deblock.exercise.adapter.out.supplier.toughjet;

import java.time.LocalDate;

public record ToughJetRequest(
        String from,
        String to,
        LocalDate outboundDate,
        LocalDate inboundDate,
        int numberOfAdults
) {}
