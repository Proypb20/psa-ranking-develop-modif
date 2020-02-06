package com.psa.ranking.repository;
import com.psa.ranking.domain.PlayerDetailPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlayerDetailPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayerDetailPointRepository extends JpaRepository<PlayerDetailPoint, Long>, JpaSpecificationExecutor<PlayerDetailPoint> {

}
