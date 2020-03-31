package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.Category;
import com.ar.pbpoints.domain.Tournament;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {


	@Query("select t from Category t where t.order = (select max(u.order) from Category u where u.tournament.id = ?1) and t.tournament.id = ?1")
	Category LastCategoryByTournamentId(Long tournamentId);
}
