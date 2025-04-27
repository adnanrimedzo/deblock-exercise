package org.deblock.exercise.adapter.out.supplier.toughjet;

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
public class ToughJetFlightMapperImpl implements ToughJetFlightMapper {

    @Override
    public ToughJetRequest toToughJetRequest(FlightSearchCriteriaDto request) {
        if ( request == null ) {
            return null;
        }

        String from = null;
        String to = null;
        LocalDate outboundDate = null;
        LocalDate inboundDate = null;
        int numberOfAdults = 0;

        from = request.origin();
        to = request.destination();
        outboundDate = request.departureDate();
        inboundDate = request.returnDate();
        numberOfAdults = request.numberOfPassengers();

        ToughJetRequest toughJetRequest = new ToughJetRequest( from, to, outboundDate, inboundDate, numberOfAdults );

        return toughJetRequest;
    }

    @Override
    public FlightDto toFlight(ToughJetResponse response) {
        if ( response == null ) {
            return null;
        }

        String airline = null;
        String departureAirportCode = null;
        String destinationAirportCode = null;

        airline = response.carrier();
        departureAirportCode = response.departureAirportName();
        destinationAirportCode = response.arrivalAirportName();

        String supplier = "ToughJet";
        BigDecimal fare = toughJetFare(response);
        LocalDateTime departureDate = toLocalDateTime(response.outboundDateTime());
        LocalDateTime arrivalDate = toLocalDateTime(response.inboundDateTime());

        FlightDto flightDto = new FlightDto( airline, supplier, fare, departureAirportCode, destinationAirportCode, departureDate, arrivalDate );

        return flightDto;
    }
}
