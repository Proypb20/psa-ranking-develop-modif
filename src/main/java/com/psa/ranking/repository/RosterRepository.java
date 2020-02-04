package com.psa.ranking.repository;
import com.psa.ranking.domain.Roster;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Roster entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RosterRepository extends JpaRepository<Roster, Long> {

}
