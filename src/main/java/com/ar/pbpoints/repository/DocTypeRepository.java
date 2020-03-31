package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.DocType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DocType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocTypeRepository extends JpaRepository<DocType, Long> {

}
