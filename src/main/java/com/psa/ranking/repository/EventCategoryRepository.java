package com.psa.ranking.repository;
import com.psa.ranking.domain.EventCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EventCategory entity.
 */
@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long>, JpaSpecificationExecutor<EventCategory> {

    Optional<List<EventCategory>> findByEvent_EndInscriptionDate (LocalDate localDate);
}
