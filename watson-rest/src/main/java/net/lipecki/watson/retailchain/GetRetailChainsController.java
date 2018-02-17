package net.lipecki.watson.retailchain;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.reatialchain.GetRetailChainsQuery;
import net.lipecki.watson.reatialchain.RetailChain;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetRetailChainsController {

    private final GetRetailChainsQuery query;

    public GetRetailChainsController(final GetRetailChainsQuery query) {
        this.query = query;
    }

    @GetMapping("/retailchain")
    @Transactional
    public List<RetailChain> getRetailChains() {
        return this.query.getRetailChains();
    }

}
