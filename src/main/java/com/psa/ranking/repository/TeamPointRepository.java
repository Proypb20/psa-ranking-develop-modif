package com.psa.ranking.repository;
import com.psa.ranking.domain.TeamPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TeamPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamPointRepository extends JpaRepository<TeamPoint, Long>, JpaSpecificationExecutor<TeamPoint> {

}
