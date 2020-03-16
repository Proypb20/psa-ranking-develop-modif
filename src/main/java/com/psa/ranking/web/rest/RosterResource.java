package com.psa.ranking.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.psa.ranking.domain.User;
import com.psa.ranking.service.RosterQueryService;
import com.psa.ranking.service.RosterService;
import com.psa.ranking.service.dto.RosterCriteria;
import com.psa.ranking.service.dto.RosterDTO;
import com.psa.ranking.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.psa.ranking.domain.Roster}.
 */
@RestController
@RequestMapping("/api")
public class RosterResource {

    private final Logger log = LoggerFactory.getLogger(RosterResource.class);

    private static final String ENTITY_NAME = "roster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RosterService rosterService;

    private final RosterQueryService rosterQueryService;

    public RosterResource(RosterService rosterService, RosterQueryService rosterQueryService) {
        this.rosterService = rosterService;
        this.rosterQueryService = rosterQueryService;
    }

    /**
     * {@code POST  /rosters} : Create a new roster.
     *
     * @param rosterDTO the rosterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rosterDTO, or with status {@code 400 (Bad Request)} if the roster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rosters")
    public ResponseEntity<RosterDTO> createRoster(@Valid @RequestBody RosterDTO rosterDTO) throws URISyntaxException {
        log.debug("REST request to save Roster : {}", rosterDTO);
        if (rosterDTO.getId() != null) {
            throw new BadRequestAlertException("A new roster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rosterDTO.setActive(true);
        RosterDTO result = rosterService.save(rosterDTO);
        return ResponseEntity.created(new URI("/api/rosters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rosters} : Updates an existing roster.
     *
     * @param rosterDTO the rosterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rosterDTO,
     * or with status {@code 400 (Bad Request)} if the rosterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rosterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rosters")
    public ResponseEntity<RosterDTO> updateRoster(@Valid @RequestBody RosterDTO rosterDTO) throws URISyntaxException {
        log.debug("REST request to update Roster : {}", rosterDTO);
        if (rosterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RosterDTO result = rosterService.save(rosterDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rosterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rosters} : get all the rosters.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rosters in body.
     */
    @GetMapping("/rosters")
    public ResponseEntity<List<RosterDTO>> getAllRosters(RosterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rosters by criteria: {}", criteria);
        Page<RosterDTO> page = rosterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /rosters/count} : count all the rosters.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/rosters/count")
    public ResponseEntity<Long> countRosters(RosterCriteria criteria) {
        log.debug("REST request to count Rosters by criteria: {}", criteria);
        return ResponseEntity.ok().body(rosterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rosters/:id} : get the "id" roster.
     *
     * @param id the id of the rosterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rosterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rosters/{id}")
    public ResponseEntity<RosterDTO> getRoster(@PathVariable Long id) {
        log.debug("REST request to get Roster : {}", id);
        Optional<RosterDTO> rosterDTO = rosterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rosterDTO);
    }

    /**
     * {@code DELETE  /rosters/:id} : delete the "id" roster.
     *
     * @param id the id of the rosterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rosters/{id}")
    public ResponseEntity<Void> deleteRoster(@PathVariable Long id) {
        log.debug("REST request to delete Roster : {}", id);
        rosterService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
    
    /**
      * {@code GET  /rosters} : get all the rosters for the {@link User} logged.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rosters in body.
     */
    @GetMapping("/rosters/owner")
    public ResponseEntity<List<RosterDTO>> getRosterByLoggedUser() {
        log.debug("REST request to get Rosters by User Logged");
        Optional<List<RosterDTO>> rostersDTO = rosterService.findByLogguedUser();
        return ResponseUtil.wrapOrNotFound(rostersDTO);
    }
    
    /**
     * {@code GET  /rosters} : get all the rosters for the {@link EventCategory} available.
    *
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rosters in body.
    */
   @GetMapping("/rosters/event-category/available")
   public ResponseEntity<List<RosterDTO>> findAvailableByEventCategory(@RequestParam(value="idEventCategory", required=true) Long idEventCategory){
       log.debug("REST request to get Rosters Available for EventCategory");
       if (idEventCategory == null) {
           throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
       }
       Optional<List<RosterDTO>> rostersDTO = rosterService.findAvailableByEventCategory(idEventCategory);
       return ResponseUtil.wrapOrNotFound(rostersDTO);
   }
}
