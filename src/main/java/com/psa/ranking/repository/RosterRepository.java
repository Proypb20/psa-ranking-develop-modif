package com.psa.ranking.repository;
import com.psa.ranking.domain.Roster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Roster entity.
 */
@Repository
public interface RosterRepository extends JpaRepository<Roster, Long> {

    @Query(value = "select distinct roster from Roster roster left join fetch roster.players",
        countQuery = "select count(distinct roster) from Roster roster")
    Page<Roster> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct roster from Roster roster left join fetch roster.players")
    List<Roster> findAllWithEagerRelationships();

    @Query("select roster from Roster roster left join fetch roster.players where roster.id =:id")
    Optional<Roster> findOneWithEagerRelationships(@Param("id") Long id);

}
