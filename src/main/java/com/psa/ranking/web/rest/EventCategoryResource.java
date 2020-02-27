package com.psa.ranking.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.psa.ranking.service.EventCategoryQueryService;
import com.psa.ranking.service.EventCategoryService;
import com.psa.ranking.service.dto.EventCategoryCriteria;
import com.psa.ranking.service.dto.EventCategoryDTO;
import com.psa.ranking.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.psa.ranking.domain.EventCategory}.
 */
@RestController
@RequestMapping("/api")
public class EventCategoryResource {

    private final Logger log = LoggerFactory.getLogger(EventCategoryResource.class);

    private static final String ENTITY_NAME = "eventCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCategoryService eventCategoryService;

    private final EventCategoryQueryService eventCategoryQueryService;

    public EventCategoryResource(EventCategoryService eventCategoryService, EventCategoryQueryService eventCategoryQueryService) {
        this.eventCategoryService = eventCategoryService;
        this.eventCategoryQueryService = eventCategoryQueryService;
    }

    /**
     * {@code POST  /event-categories} : Create a new eventCategory.
     *
     * @param eventCategoryDTO the eventCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventCategoryDTO, or with status {@code 400 (Bad Request)} if the eventCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-categories")
    public ResponseEntity<EventCategoryDTO> createEventCategory(@Valid @RequestBody EventCategoryDTO eventCategoryDTO) throws URISyntaxException {
        log.debug("REST request to save EventCategory : {}", eventCategoryDTO);
        if (eventCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCategoryDTO result = eventCategoryService.save(eventCategoryDTO);
        return ResponseEntity.created(new URI("/api/event-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-categories} : Updates an existing eventCategory.
     *
     * @param eventCategoryDTO the eventCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the eventCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-categories")
    public ResponseEntity<EventCategoryDTO> updateEventCategory(@Valid @RequestBody EventCategoryDTO eventCategoryDTO) throws URISyntaxException {
        log.debug("REST request to update EventCategory : {}", eventCategoryDTO);
        if (eventCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EventCategoryDTO result = eventCategoryService.save(eventCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /event-categories} : get all the eventCategories.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventCategories in body.
     */ 
    @GetMapping("/event-categories")
    public ResponseEntity<List<EventCategoryDTO>> getAllEventCategories(EventCategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EventCategories by criteria: {}", criteria);
        Page<EventCategoryDTO> page = eventCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /event-categories/count} : count all the eventCategories.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/event-categories/count")
    public ResponseEntity<Long> countEventCategories(EventCategoryCriteria criteria) {
        log.debug("REST request to count EventCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-categories/:id} : get the "id" eventCategory.
     *
     * @param id the id of the eventCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-categories/{id}")
    public ResponseEntity<EventCategoryDTO> getEventCategory(@PathVariable Long id) {
        log.debug("REST request to get EventCategory : {}", id);
        Optional<EventCategoryDTO> eventCategoryDTO = eventCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventCategoryDTO);
    }

    /**
     * {@code DELETE  /event-categories/:id} : delete the "id" eventCategory.
     *
     * @param id the id of the eventCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-categories/{id}")
    public ResponseEntity<Void> deleteEventCategory(@PathVariable Long id) {
        log.debug("REST request to delete EventCategory : {}", id);
        eventCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
    
    @PostMapping("/event-categories/fixture({idEventCategory})")
    public ResponseEntity<String> createEventCategoryFixture(@PathVariable Long idEventCategory) throws URISyntaxException {
        log.debug("REST request to generar a fixture from: {}", idEventCategory);
        if (idEventCategory == null) {
            throw new BadRequestAlertException("A eventCategory cannot have an empty ID", ENTITY_NAME, "idexists");
        }
        try {
            eventCategoryService.generarFixture(idEventCategory);
        } catch (NoResultException e) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body("Fixture generado con éxito");
    }
}
