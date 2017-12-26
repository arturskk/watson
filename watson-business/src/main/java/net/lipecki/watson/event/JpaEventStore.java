package net.lipecki.watson.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.WatsonExceptionCode;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
public class JpaEventStore implements EventStore {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public JpaEventStore(final EventRepository eventRepository, final ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Event storeEvent(final String stream, final EventPayload payload) {
        return storeEvent(stream, UUID.randomUUID(), payload);
    }

    @Override
    public Event storeEvent(final String stream, final UUID streamId, final EventPayload payload) {
        try {
            final EventEntity entity = EventEntity.builder()
                    .payload(objectMapper.writeValueAsString(payload))
                    .payloadClass(payload.getClass().getTypeName())
                    .streamId(streamId.toString())
                    .stream(stream)
                    .type(payload.getClass().getTypeName())
                    .timestamp(System.currentTimeMillis())
                    .build();
            this.eventRepository.save(entity);
            return asEvent(entity);
        } catch (final Exception ex) {
            throw WatsonException.of(WatsonExceptionCode.UNKNOWN, "Can't store event", ex)
                    .with("stream", stream)
                    .with("payload", payload);
        }
    }

    @Override
    public Stream<Event> getEventsByStream(final List<String> streams) {
        return this.eventRepository
                .findByStreamIn(streams)
                .stream()
                .map(this::asEvent);
    }

    @Override
    public Stream<Event> getEvents() {
        return this.eventRepository
                .findAll()
                .stream()
                .map(this::asEvent);
    }

    private <T extends EventPayload> Event asEvent(final EventEntity entity) {
        try {
            final Class<T> payloadClass = (Class<T>) Class.forName(entity.getPayloadClass());
            final T payload = objectMapper.readValue(
                    entity.getPayload(),
                    payloadClass
            );
            return Event.<T>builder()
                    .stream(entity.getStream())
                    .streamId(entity.getStreamId())
                    .sequenceId(entity.getId())
                    .timestamp(entity.getTimestamp())
                    .type(entity.getType())
                    .payload(payload)
                    .build();
        } catch (final Exception ex) {
            throw WatsonException
                    .of(WatsonExceptionCode.UNKNOWN, "Can't parse event entity as event", ex)
                    .with("entity", entity);
        }
    }

}
