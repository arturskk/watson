package net.lipecki.watson.combiner;

import net.lipecki.watson.event.Event;

import java.util.Map;
import java.util.function.BiConsumer;

public interface AggregateCombinerHandler<T> extends BiConsumer<Map<String, T>, Event<?>> {
}
