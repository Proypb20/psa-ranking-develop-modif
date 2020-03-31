package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.EventCategory;
import com.ar.pbpoints.domain.Game;

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
