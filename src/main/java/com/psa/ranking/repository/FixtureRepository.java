package com.psa.ranking.repository;
import com.psa.ranking.domain.Fixture;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Fixture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FixtureRepository extends JpaRepository<Fixture, Long>, JpaSpecificationExecutor<Fixture> {

}
