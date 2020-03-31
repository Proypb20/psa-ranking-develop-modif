package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.TeamPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TeamPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamPointRepository extends JpaRepository<TeamPoint, Long>, JpaSpecificationExecutor<TeamPoint> {

}
