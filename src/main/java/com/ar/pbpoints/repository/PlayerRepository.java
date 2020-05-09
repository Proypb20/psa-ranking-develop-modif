package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.EventCategory;
import com.ar.pbpoints.domain.Player;
import com.ar.pbpoints.domain.Roster;
import com.ar.pbpoints.domain.User;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Player entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    @Query("select player from Player player where player.user.login = ?#{principal.username}")
    List<Player> findByUserIsCurrentUser();

}
