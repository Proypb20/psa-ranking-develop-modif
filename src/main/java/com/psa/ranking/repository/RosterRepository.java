package com.psa.ranking.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Roster;

/**
 * Spring Data  repository for the Roster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RosterRepository extends JpaRepository<Roster, Long>, JpaSpecificationExecutor<Roster> {

	public List<Roster> findByEventCategory(EventCategory eventCategory);
}
