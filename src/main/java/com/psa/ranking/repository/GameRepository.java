package com.psa.ranking.repository;
import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Game;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Game entity.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

	public List<Game> findByEventCategory(EventCategory eventCategory);
}
