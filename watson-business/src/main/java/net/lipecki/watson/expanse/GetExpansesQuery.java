package net.lipecki.watson.expanse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class GetExpansesQuery {

    private final ExpanseStore expanseStore;

    public GetExpansesQuery(final ExpanseStore expanseStore) {
        this.expanseStore = expanseStore;
    }

    public List<Expanse> getExpanses(final LocalDate from, final LocalDate to) {
        return this.expanseStore.getExpanses(from, to);
    }

}
