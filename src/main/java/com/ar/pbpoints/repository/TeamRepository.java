package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.Player;
import com.ar.pbpoints.domain.Roster;
import com.ar.pbpoints.domain.Team;
import com.ar.pbpoints.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

    @Query("select team from Team team where team.owner.login = ?#{principal.username}")
    List<Team> findByOwnerIsCurrentUser();

    Optional<Team> findByName (String name);

    @Query("select team from Team team where team.name = ?1 and team.owner.id = ?2")
    Optional<Team> findByNameAndIdOwner(String name, Long ownerId);
}
