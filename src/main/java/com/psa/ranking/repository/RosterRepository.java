package com.psa.ranking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Roster;
import com.psa.ranking.domain.User;

/**
 * Spring Data repository for the Roster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RosterRepository extends JpaRepository<Roster, Long>, JpaSpecificationExecutor<Roster> {

    public List<Roster> findByEventCategory(EventCategory eventCategory);

    public Optional<List<Roster>> findByTeam_Owner(User user);
    
    public Optional<List<Roster>> findByNotIn(List<Long> ids);
    
    @Query("select count(1) from Player player, PlayerPoint playerpoint where player.user_id = playerpoint.user_id and player.roster_id = ?1 and playerpoint.category_id = ?2")
    int CountPlayerNextCategory(Long rosterId, Long categoryId);
}
