package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.Province;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Province entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long>, JpaSpecificationExecutor<Province> {

}
