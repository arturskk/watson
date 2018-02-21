package net.lipecki.watson.reatialchain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GetRetailChainQuery {

    private RetailChainStore store;

    public GetRetailChainQuery(RetailChainStore store) {
        this.store = store;
    }

    public Optional<RetailChain> getRetailChain(String uuid) {
        return store.getRetailChain(uuid);
    }

}
