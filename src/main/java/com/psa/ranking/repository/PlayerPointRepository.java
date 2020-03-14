package com.psa.ranking.repository;
import com.psa.ranking.domain.PlayerPoint;
import com.psa.ranking.domain.Tournament;
import com.psa.ranking.domain.User;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PlayerPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerPointRepository extends JpaRepository<PlayerPoint, Long>, JpaSpecificationExecutor<PlayerPoint> {

    @Query("select playerPoint from PlayerPoint playerPoint where playerPoint.user.login = ?#{principal.username}")
    List<PlayerPoint> findByUserIsCurrentUser();
    
    PlayerPoint findByUserAndTournament(User user,Tournament tournament);

}
