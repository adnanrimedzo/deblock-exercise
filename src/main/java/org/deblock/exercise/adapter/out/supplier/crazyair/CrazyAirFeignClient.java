package org.deblock.exercise.adapter.out.supplier.crazyair;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "crazyAirClient", url = "${supplier.crazy-air.url}")
public interface CrazyAirFeignClient {
    @PostMapping("/flights")
    List<CrazyAirResponse> search(@RequestBody final CrazyAirRequest request);
}
