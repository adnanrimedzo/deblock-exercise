package org.deblock.exercise.adapter.out.supplier.crazyair;

import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.deblock.exercise.domain.model.FlightDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface CrazyAirFlightMapper {
    CrazyAirFlightMapper INSTANCE = Mappers.getMapper(CrazyAirFlightMapper.class);

    @Mapping(target = "passengerCount", source = "numberOfPassengers")
    CrazyAirRequest toCrazyAirRequest(final FlightSearchCriteriaDto request);

    @Mapping(target = "supplier", constant = "CrazyAir")
    @Mapping(target = "fare", source = "price")
    @BeanMapping(ignoreUnmappedSourceProperties = {"cabinclass"})
    FlightDto toFlight(final CrazyAirResponse response);
}
