package org.deblock.exercise.application.port.out;

import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;

import java.util.List;

public interface SupplierPort {
    List<FlightDto> getFlights(final FlightSearchCriteriaDto request);
}
