package org.deblock.exercise.application.port.in;

import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.deblock.exercise.domain.model.FlightDto;
import java.util.List;

public interface FlightSearchUseCase {
    List<FlightDto> searchFlights(final FlightSearchCriteriaDto request);
}
