package com.ar.pbpoints.repository;
import com.ar.pbpoints.domain.Category;
import com.ar.pbpoints.domain.Event;
import com.ar.pbpoints.domain.EventCategory;
import com.ar.pbpoints.domain.Roster;

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

    public List<EventCategory> findByEvent(Event event);

    EventCategory findByRosters(Roster roster);

    Optional<EventCategory> findByEventAndCategory(Event event, Category category);
}
