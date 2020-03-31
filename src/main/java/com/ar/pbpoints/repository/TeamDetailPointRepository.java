package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.TeamDetailPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TeamDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamDetailPointRepository extends JpaRepository<TeamDetailPoint, Long>, JpaSpecificationExecutor<TeamDetailPoint> {

}
