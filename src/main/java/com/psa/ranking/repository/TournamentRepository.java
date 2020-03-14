package com.psa.ranking.repository;
import com.psa.ranking.domain.Event;
import com.psa.ranking.domain.Tournament;
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
    
    Tournament findByEvent(Event event);

}
