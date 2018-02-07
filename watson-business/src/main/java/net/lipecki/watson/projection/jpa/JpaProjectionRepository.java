package net.lipecki.watson.projection.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProjectionRepository extends JpaRepository<JpaProjectionThreadStatus, String> {

    Optional<JpaProjectionThreadStatus> findOneById(String id);

}
