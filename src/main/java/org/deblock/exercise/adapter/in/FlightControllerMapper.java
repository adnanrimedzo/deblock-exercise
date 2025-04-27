package org.deblock.exercise.adapter.in;

import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface FlightControllerMapper {
    FlightControllerMapper INSTANCE = Mappers.getMapper(FlightControllerMapper.class);

    FlightSearchCriteriaDto toFlightSearchCriteria(final FlightSearchRequest request);
    FlightSearchResponse toFlightSearchResponse(final FlightDto flightDto);
}
