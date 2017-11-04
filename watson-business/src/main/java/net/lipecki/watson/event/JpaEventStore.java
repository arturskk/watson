package net.lipecki.watson.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.WatsonExceptionCode;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class JpaEventStore implements EventStore {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    public JpaEventStore(final EventRepository eventRepository, final ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final String type, final T payload) {
        return storeEvent(stream, UUID.randomUUID().toString(), type, payload);
    }

    @Override
    public <T> Event<T> storeEvent(final String stream, final String streamId, final String type, final T payload) {
        try {
            final EventEntity entity = EventEntity.builder()
                    .payload(objectMapper.writeValueAsString(payload))
                    .payloadClass(payload.getClass().getTypeName())
                    .streamId(streamId)
                    .stream(stream)
                    .type(type)
                    .timestamp(System.currentTimeMillis())
                    .build();
            this.eventRepository.save(entity);
            return asEvent(entity);
        } catch (final Exception ex) {
            throw WatsonException.of(WatsonExceptionCode.UNKNOWN, "Can't store event", ex)
                    .with("stream", stream)
                    .with("type", type)
                    .with("payload", payload);
        }
    }

    @Override
    public List<Event<?>> getEventsByStream(final String stream) {
        return this.eventRepository
                .findByStream(stream)
                .stream()
                .map(this::asEvent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event<?>> getEvents() {
        return this.eventRepository
                .findAll()
                .stream()
                .map(this::asEvent)
                .collect(Collectors.toList());
    }

    private <T> Event<T> asEvent(final EventEntity entity) {
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
