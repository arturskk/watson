package net.lipecki.watson.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findByStreamIn(final List<String> streams);

    @Query("select e from events e")
    Stream<EventEntity> findAllAndStream();

}
