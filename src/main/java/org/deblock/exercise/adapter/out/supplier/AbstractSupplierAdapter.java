package org.deblock.exercise.adapter.out.supplier;

import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.deblock.exercise.application.port.out.SupplierPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
public abstract class AbstractSupplierAdapter<TRequest, TResponse> implements SupplierPort {
    public abstract List<FlightDto> getFlights(final FlightSearchCriteriaDto request);

    protected List<FlightDto> processSearch(final FlightSearchCriteriaDto request) {
        final TRequest supplierRequest = mapToSupplierRequest(request);
        final List<TResponse> supplierResponses = callSupplier(supplierRequest);
        return supplierResponses.stream().map(this::mapToFlightDto).toList();
    }

    public List<FlightDto> searchFallback(final HttpClientErrorException ex) {
        return defaultFallback(ex);
    }

    public List<FlightDto> searchFallback(final FeignException.FeignClientException ex) {
        return defaultFallback(ex);
    }

    public List<FlightDto> searchFallback(final FeignException.FeignServerException ex) {
        return defaultFallback(ex);
    }

    public List<FlightDto> searchFallback(final RetryableException ex) {
        return defaultFallback(ex);
    }

    public List<FlightDto> searchFallback(final CallNotPermittedException ex) {
        return defaultFallback(ex);
    }

    private List<FlightDto> defaultFallback(final Exception ex) {
        log.error("{} search failed, fallback activated: {}", getSupplierName(), ex.getMessage());
        return emptyList();
    }

    protected abstract String getSupplierName();

    protected abstract TRequest mapToSupplierRequest(FlightSearchCriteriaDto request);

    protected abstract List<TResponse> callSupplier(TRequest request);

    protected abstract FlightDto mapToFlightDto(TResponse response);
}
