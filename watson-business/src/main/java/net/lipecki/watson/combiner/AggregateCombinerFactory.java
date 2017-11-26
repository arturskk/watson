package net.lipecki.watson.combiner;

import java.util.Map;
import java.util.function.Supplier;

public interface AggregateCombinerFactory {
    <T> AggregateCombiner<T> getAggregateCombiner(String stream);

    <T> AggregateCombiner<T> getAggregateCombiner(String stream, Supplier<Map<String, T>> initializer);
}
