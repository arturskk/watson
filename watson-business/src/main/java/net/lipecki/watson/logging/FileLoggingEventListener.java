package net.lipecki.watson.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import net.lipecki.watson.WatsonExceptionCode;
import net.lipecki.watson.event.Event;
import net.lipecki.watson.event.EventPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

@Slf4j
@Service
@ConditionalOnProperty("event.logging.file")
public class FileLoggingEventListener {

    public static final String THREAD_PREFIX = "EventFileLog-";
    private final ObjectMapper objectMapper;

    private ThreadPoolTaskExecutor singleThreadTaskExecutor;
    private final File loggingFile;

    public FileLoggingEventListener(
            @Value("${event.logging.file}") final String loggingFileName,
            final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.singleThreadTaskExecutor = initializeSingleThreadedTaskExecutor();
        this.loggingFile = new File(loggingFileName);

        if (this.loggingFile.exists() && !this.loggingFile.isFile()) {
            throw WatsonException.of(
                    WatsonExceptionCode.UNKNOWN, "Can't initialize FileLoggingEventListener due to non file resource"
            ).with(
                    "loggingFileName",
                    loggingFileName
            );
        }
    }

    @EventListener
    public synchronized <T extends EventPayload> void handleEventStored(final Event event) {
        this.singleThreadTaskExecutor.execute(
                () -> {
                    try (final BufferedWriter output = new BufferedWriter(new FileWriter(this.loggingFile, true))) {
                        output.write(asEventLogLine(event));
                        output.newLine();
                    } catch (final Exception exception) {
                        throw WatsonException.of(
                                WatsonExceptionCode.UNKNOWN,
                                "Can't store event as file log line",
                                exception
                        ).with(
                                "evnet",
                                event
                        );
                    }
                }
        );
    }

    private String asEventLogLine(final Event event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event.asMap());
    }

    private ThreadPoolTaskExecutor initializeSingleThreadedTaskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setThreadNamePrefix(THREAD_PREFIX);
        taskExecutor.setThreadGroupName(getClass().getSimpleName());
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.initialize();
        return taskExecutor;
    }

}
