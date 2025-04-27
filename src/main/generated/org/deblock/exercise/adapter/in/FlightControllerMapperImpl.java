package org.deblock.exercise.adapter.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-27T20:26:22+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Microsoft)"
)
public class FlightControllerMapperImpl implements FlightControllerMapper {

    @Override
    public FlightSearchCriteriaDto toFlightSearchCriteria(FlightSearchRequest request) {
        if ( request == null ) {
            return null;
        }

        String origin = null;
        String destination = null;
        LocalDate departureDate = null;
        LocalDate returnDate = null;
        int numberOfPassengers = 0;

        origin = request.origin();
        destination = request.destination();
        departureDate = request.departureDate();
        returnDate = request.returnDate();
        if ( request.numberOfPassengers() != null ) {
            numberOfPassengers = request.numberOfPassengers();
        }

        FlightSearchCriteriaDto flightSearchCriteriaDto = new FlightSearchCriteriaDto( origin, destination, departureDate, returnDate, numberOfPassengers );

        return flightSearchCriteriaDto;
    }

    @Override
    public FlightSearchResponse toFlightSearchResponse(FlightDto flightDto) {
        if ( flightDto == null ) {
            return null;
        }

        String airline = null;
        String supplier = null;
        BigDecimal fare = null;
        String departureAirportCode = null;
        String destinationAirportCode = null;
        LocalDateTime departureDate = null;
        LocalDateTime arrivalDate = null;

        airline = flightDto.airline();
        supplier = flightDto.supplier();
        fare = flightDto.fare();
        departureAirportCode = flightDto.departureAirportCode();
        destinationAirportCode = flightDto.destinationAirportCode();
        departureDate = flightDto.departureDate();
        arrivalDate = flightDto.arrivalDate();

        FlightSearchResponse flightSearchResponse = new FlightSearchResponse( airline, supplier, fare, departureAirportCode, destinationAirportCode, departureDate, arrivalDate );

        return flightSearchResponse;
    }
}
