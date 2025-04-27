package org.deblock.exercise.adapter.out.supplier.crazyair;

import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.springframework.stereotype.Component;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.deblock.exercise.adapter.out.supplier.AbstractSupplierAdapter;

@Slf4j
@Component
public class CrazyAirSupplierAdapter extends AbstractSupplierAdapter<CrazyAirRequest, CrazyAirResponse> {
    private final CrazyAirFeignClient client;

    public CrazyAirSupplierAdapter(final CrazyAirFeignClient client) {
        this.client = client;
    }

    @Override
    @CircuitBreaker(name = "crazyAirSearch", fallbackMethod = "searchFallback")
    public List<FlightDto> getFlights(final FlightSearchCriteriaDto request) {
        return processSearch(request);
    }

    @Override
    protected String getSupplierName() {
        return "CrazyAir";
    }

    @Override
    protected CrazyAirRequest mapToSupplierRequest(final FlightSearchCriteriaDto request) {
        return CrazyAirFlightMapper.INSTANCE.toCrazyAirRequest(request);
    }

    @Override
    protected List<CrazyAirResponse> callSupplier(final CrazyAirRequest request) {
        return client.search(request);
    }

    @Override
    protected FlightDto mapToFlightDto(final CrazyAirResponse response) {
        return CrazyAirFlightMapper.INSTANCE.toFlight(response);
    }
}
