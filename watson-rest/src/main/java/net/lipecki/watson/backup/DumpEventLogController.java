package net.lipecki.watson.backup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventStore;
import net.lipecki.watson.rest.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class DumpEventLogController {

    private final EventStore eventStore;
    private final ObjectMapper objectMapper;

    public DumpEventLogController(final EventStore eventStore, final ObjectMapper objectMapper) {
        this.eventStore = eventStore;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/backup/events/dump")
    @Transactional(readOnly = true)
    public void getAllEventsDump(final HttpServletResponse response) {
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.addHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=events." + System.currentTimeMillis() + ".dump"
        );
        response.setCharacterEncoding("UTF-8");

        try(final Stream<Event> events = this.eventStore.getEvents()) {
            final PrintWriter out = response.getWriter();
            events.map(Event::asMap)
                    .map(this::asJsonString)
                    .map(line -> line + "\n")
                    .forEach(out::append);
            out.flush();
        } catch (IOException e) {
            throw WatsonException.of("Can't write events dump", e);
        }
    }

    private String asJsonString(final Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (final JsonProcessingException ex) {
            throw WatsonException.of("Can't format as json string", ex);
        }
    }

}
