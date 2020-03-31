package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.PlayerPoint;
import com.ar.pbpoints.domain.Tournament;
import com.ar.pbpoints.domain.User;

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
