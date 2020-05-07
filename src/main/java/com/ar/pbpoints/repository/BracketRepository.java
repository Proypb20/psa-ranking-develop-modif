package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.Bracket;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Bracket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BracketRepository extends JpaRepository<Bracket, Long> {

}
