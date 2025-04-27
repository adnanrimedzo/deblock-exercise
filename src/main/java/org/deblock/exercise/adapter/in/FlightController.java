package org.deblock.exercise.adapter.in;

import org.deblock.exercise.application.port.in.FlightSearchUseCase;
import org.deblock.exercise.domain.model.FlightDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightSearchUseCase flightSearchUseCase;

    public FlightController(final FlightSearchUseCase flightSearchUseCase) {
        this.flightSearchUseCase = flightSearchUseCase;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightSearchResponse>> searchFlights(@Valid @ModelAttribute final FlightSearchRequest request) {
        final List<FlightDto> flightDtos = flightSearchUseCase.searchFlights(FlightControllerMapper.INSTANCE.toFlightSearchCriteria(request));
        final List<FlightSearchResponse> response = flightDtos.stream()
                .map(FlightControllerMapper.INSTANCE::toFlightSearchResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
