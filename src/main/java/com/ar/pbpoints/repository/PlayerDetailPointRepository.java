package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.PlayerDetailPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlayerDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerDetailPointRepository extends JpaRepository<PlayerDetailPoint, Long>, JpaSpecificationExecutor<PlayerDetailPoint> {

}
