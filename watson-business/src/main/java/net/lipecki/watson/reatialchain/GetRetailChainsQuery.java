package net.lipecki.watson.reatialchain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GetRetailChainsQuery {

    private RetailChainStore store;

    public GetRetailChainsQuery(RetailChainStore store) {
        this.store = store;
    }

    public List<RetailChain> getRetailChains() {
        return store.getRetailChains();
    }

}
