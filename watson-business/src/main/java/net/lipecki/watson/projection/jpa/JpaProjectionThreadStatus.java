package net.lipecki.watson.projection.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity(name = "projection_threads")
@Table(
        indexes = {
                @Index(name = "_idx_projection_thread_id", columnList = "id"),
        }
)
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class JpaProjectionThreadStatus {

    @Id
    private String id;
    private Long lastSequenceId;

}
