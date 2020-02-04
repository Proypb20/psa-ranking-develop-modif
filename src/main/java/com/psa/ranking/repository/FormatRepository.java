package com.psa.ranking.repository;
import com.psa.ranking.domain.Format;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Format entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormatRepository extends JpaRepository<Format, Long>, JpaSpecificationExecutor<Format> {

}
