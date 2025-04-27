package org.deblock.exercise.adapter.out.supplier.crazyair;

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
public class CrazyAirFlightMapperImpl implements CrazyAirFlightMapper {

    @Override
    public CrazyAirRequest toCrazyAirRequest(FlightSearchCriteriaDto request) {
        if ( request == null ) {
            return null;
        }

        int passengerCount = 0;
        String origin = null;
        String destination = null;
        LocalDate departureDate = null;
        LocalDate returnDate = null;

        passengerCount = request.numberOfPassengers();
        origin = request.origin();
        destination = request.destination();
        departureDate = request.departureDate();
        returnDate = request.returnDate();

        CrazyAirRequest crazyAirRequest = new CrazyAirRequest( origin, destination, departureDate, returnDate, passengerCount );

        return crazyAirRequest;
    }

    @Override
    public FlightDto toFlight(CrazyAirResponse response) {
        if ( response == null ) {
            return null;
        }

        BigDecimal fare = null;
        String airline = null;
        String departureAirportCode = null;
        String destinationAirportCode = null;
        LocalDateTime departureDate = null;
        LocalDateTime arrivalDate = null;

        fare = response.price();
        airline = response.airline();
        departureAirportCode = response.departureAirportCode();
        destinationAirportCode = response.destinationAirportCode();
        departureDate = response.departureDate();
        arrivalDate = response.arrivalDate();

        String supplier = "CrazyAir";

        FlightDto flightDto = new FlightDto( airline, supplier, fare, departureAirportCode, destinationAirportCode, departureDate, arrivalDate );

        return flightDto;
    }
}
