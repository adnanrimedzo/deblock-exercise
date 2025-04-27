package org.deblock.exercise.adapter.out.supplier.toughjet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "toughJetClient", url = "${supplier.tough-jet.url}")
public interface ToughJetFeignClient {
    @PostMapping("/flights")
    List<ToughJetResponse> search(@RequestBody final ToughJetRequest request);
}
