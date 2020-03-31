package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.Event;
import com.ar.pbpoints.domain.Tournament;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Tournament entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament> {

    @Query("select tournament from Tournament tournament where tournament.owner.login = ?#{principal.username}")
    List<Tournament> findByOwnerIsCurrentUser();

    Tournament findByEvents(Event event);

}
