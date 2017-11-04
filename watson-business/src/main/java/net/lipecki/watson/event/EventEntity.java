package net.lipecki.watson.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity(name = "events")
@Table(
        indexes = {
                @Index(name = "_idx_events_id", columnList = "id"),
                @Index(name = "_idx_events_stream", columnList = "stream"),
                @Index(name = "_idx_events_stream_id", columnList = "streamId")
        }
)
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String stream;
    private String streamId;
    @Lob
    private String payload;
    private String payloadClass;
    private String type;
    private Long timestamp;

}
