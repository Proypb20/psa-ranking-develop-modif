package com.psa.ranking.repository;
import com.psa.ranking.domain.TeamDetailPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TeamDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamDetailPointRepository extends JpaRepository<TeamDetailPoint, Long>, JpaSpecificationExecutor<TeamDetailPoint> {

}
