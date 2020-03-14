package com.psa.ranking.repository;
import com.psa.ranking.domain.Category;
import com.psa.ranking.domain.Tournament;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

	
	@Query("select t from Category t where t.jhi_order = (select max(u.jhi_order) from Category u where u.tournament_id = ?1) and t.tournament_id = ?1")
	Category LastCategoryByTournamentId(Long tournamentId);
}
