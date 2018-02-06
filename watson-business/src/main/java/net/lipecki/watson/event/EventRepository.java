package net.lipecki.watson.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findByStreamIn(final List<String> streams);

    @Query("select e from events e")
    Stream<EventEntity> findAllAndStream();

    @Query("select e from events e where e.id > :sequenceId")
    Stream<EventEntity> findAllAndStream(@Param("sequenceId") final long sequenceId, final Pageable pageable);

    @Query("select max(e.id) from events e")
    Long getLastId();

}
