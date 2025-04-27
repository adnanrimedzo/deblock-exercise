package org.deblock.exercise.adapter.out.supplier.toughjet;

import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.deblock.exercise.domain.model.FlightDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Mapper(unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface ToughJetFlightMapper {
    ToughJetFlightMapper INSTANCE = Mappers.getMapper(ToughJetFlightMapper.class);

    @Mapping(target = "from", source = "origin")
    @Mapping(target = "to", source = "destination")
    @Mapping(target = "outboundDate", source = "departureDate")
    @Mapping(target = "inboundDate", source = "returnDate")
    @Mapping(target = "numberOfAdults", source = "numberOfPassengers")
    ToughJetRequest toToughJetRequest(final FlightSearchCriteriaDto request);

    @Mapping(target = "airline", source = "carrier")
    @Mapping(target = "supplier", constant = "ToughJet")
    @Mapping(target = "fare", expression = "java(toughJetFare(response))" )
    @Mapping(target = "departureAirportCode", source = "departureAirportName")
    @Mapping(target = "destinationAirportCode", source = "arrivalAirportName")
    @Mapping(target = "departureDate", expression = "java(toLocalDateTime(response.outboundDateTime()))")
    @Mapping(target = "arrivalDate", expression = "java(toLocalDateTime(response.inboundDateTime()))")
    @BeanMapping(ignoreUnmappedSourceProperties = {"basePrice", "tax", "discount", "outboundDateTime", "inboundDateTime"})
    FlightDto toFlight(final ToughJetResponse response);

    default BigDecimal toughJetFare(final ToughJetResponse response) {
        final var basePlusTax = response.basePrice().add(response.tax());
        final var discountFactor = BigDecimal.ONE.subtract(response.discount().divide(BigDecimal.valueOf(100)));
        return basePlusTax.multiply(discountFactor);
    }
    
    default LocalDateTime toLocalDateTime(final Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant, java.time.ZoneOffset.UTC);
    }
}
