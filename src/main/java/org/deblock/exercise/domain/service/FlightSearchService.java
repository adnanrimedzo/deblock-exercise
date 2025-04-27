package org.deblock.exercise.domain.service;

import org.deblock.exercise.domain.model.FlightDto;
import org.deblock.exercise.domain.model.FlightSearchCriteriaDto;
import org.deblock.exercise.application.port.in.FlightSearchUseCase;
import org.deblock.exercise.application.port.out.SupplierPort;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public final class FlightSearchService implements FlightSearchUseCase {
    private final List<SupplierPort> supplierPorts;
    private final ExecutorService executor;

    public FlightSearchService(final List<SupplierPort> supplierPorts) {
        this.supplierPorts = supplierPorts;
        this.executor = Executors.newFixedThreadPool(supplierPorts.size());
    }

    @Override
    public List<FlightDto> searchFlights(final FlightSearchCriteriaDto request) {
        final List<CompletableFuture<List<FlightDto>>> futures = supplierPorts.stream()
                .map(supplier -> CompletableFuture.supplyAsync(() -> supplier.getFlights(request), executor))
                .toList();
        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(FlightDto::fare))
                .toList();
    }
}
