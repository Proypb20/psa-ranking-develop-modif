package com.psa.ranking.repository;
import com.psa.ranking.domain.DocType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DocType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {

}
