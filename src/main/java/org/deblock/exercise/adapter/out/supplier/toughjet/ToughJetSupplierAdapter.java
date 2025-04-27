package org.deblock.exercise.adapter.out.supplier.toughjet;

import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.deblock.exercise.adapter.out.supplier.AbstractSupplierAdapter;
import org.springframework.stereotype.Component;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Slf4j
@Component
public class ToughJetSupplierAdapter extends AbstractSupplierAdapter<ToughJetRequest, ToughJetResponse> {
    private final ToughJetFeignClient client;

    public ToughJetSupplierAdapter(final ToughJetFeignClient client) {
        this.client = client;
    }

    @Override
    @CircuitBreaker(name = "toughJetSearch", fallbackMethod = "searchFallback")
    public List<FlightDto> getFlights(final FlightSearchCriteriaDto request) {
        return processSearch(request);
    }

    @Override
    protected String getSupplierName() {
        return "ToughJet";
    }

    @Override
    protected ToughJetRequest mapToSupplierRequest(final FlightSearchCriteriaDto request) {
        return ToughJetFlightMapper.INSTANCE.toToughJetRequest(request);
    }

    @Override
    protected List<ToughJetResponse> callSupplier(final ToughJetRequest request) {
        return client.search(request);
    }

    @Override
    protected FlightDto mapToFlightDto(final ToughJetResponse response) {
        return ToughJetFlightMapper.INSTANCE.toFlight(response);
    }
}
