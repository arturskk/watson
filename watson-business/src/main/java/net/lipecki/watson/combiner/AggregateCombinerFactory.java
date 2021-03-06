package net.lipecki.watson.combiner;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public interface AggregateCombinerFactory {

    <T> AggregateCombiner<T> getAggregateCombiner(String cacheKey, List<String> streams);

    <T> AggregateCombiner<T> getAggregateCombiner(String cacheKey, List<String> streams, Supplier<Map<String, T>> initializer);

}
