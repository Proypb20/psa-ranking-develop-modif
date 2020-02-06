package com.psa.ranking.web.rest;

import com.psa.ranking.service.TeamDetailPointService;
import com.psa.ranking.web.rest.errors.BadRequestAlertException;
import com.psa.ranking.service.dto.TeamDetailPointDTO;
import com.psa.ranking.service.dto.TeamDetailPointCriteria;
import com.psa.ranking.service.TeamDetailPointQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.psa.ranking.domain.TeamDetailPoint}.
 */
@RestController
@RequestMapping("/api")
public class TeamDetailPointResource {

    private final Logger log = LoggerFactory.getLogger(TeamDetailPointResource.class);

    private static final String ENTITY_NAME = "teamDetailPoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeamDetailPointService teamDetailPointService;

    private final TeamDetailPointQueryService teamDetailPointQueryService;

    public TeamDetailPointResource(TeamDetailPointService teamDetailPointService, TeamDetailPointQueryService teamDetailPointQueryService) {
        this.teamDetailPointService = teamDetailPointService;
        this.teamDetailPointQueryService = teamDetailPointQueryService;
    }

    /**
     * {@code POST  /team-detail-points} : Create a new teamDetailPoint.
     *
     * @param teamDetailPointDTO the teamDetailPointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teamDetailPointDTO, or with status {@code 400 (Bad Request)} if the teamDetailPoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/team-detail-points")
    public ResponseEntity<TeamDetailPointDTO> createTeamDetailPoint(@Valid @RequestBody TeamDetailPointDTO teamDetailPointDTO) throws URISyntaxException {
        log.debug("REST request to save TeamDetailPoint : {}", teamDetailPointDTO);
        if (teamDetailPointDTO.getId() != null) {
            throw new BadRequestAlertException("A new teamDetailPoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TeamDetailPointDTO result = teamDetailPointService.save(teamDetailPointDTO);
        return ResponseEntity.created(new URI("/api/team-detail-points/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /team-detail-points} : Updates an existing teamDetailPoint.
     *
     * @param teamDetailPointDTO the teamDetailPointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teamDetailPointDTO,
     * or with status {@code 400 (Bad Request)} if the teamDetailPointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teamDetailPointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/team-detail-points")
    public ResponseEntity<TeamDetailPointDTO> updateTeamDetailPoint(@Valid @RequestBody TeamDetailPointDTO teamDetailPointDTO) throws URISyntaxException {
        log.debug("REST request to update TeamDetailPoint : {}", teamDetailPointDTO);
        if (teamDetailPointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TeamDetailPointDTO result = teamDetailPointService.save(teamDetailPointDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teamDetailPointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /team-detail-points} : get all the teamDetailPoints.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teamDetailPoints in body.
     */
    @GetMapping("/team-detail-points")
    public ResponseEntity<List<TeamDetailPointDTO>> getAllTeamDetailPoints(TeamDetailPointCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TeamDetailPoints by criteria: {}", criteria);
        Page<TeamDetailPointDTO> page = teamDetailPointQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /team-detail-points/count} : count all the teamDetailPoints.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/team-detail-points/count")
    public ResponseEntity<Long> countTeamDetailPoints(TeamDetailPointCriteria criteria) {
        log.debug("REST request to count TeamDetailPoints by criteria: {}", criteria);
        return ResponseEntity.ok().body(teamDetailPointQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /team-detail-points/:id} : get the "id" teamDetailPoint.
     *
     * @param id the id of the teamDetailPointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teamDetailPointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/team-detail-points/{id}")
    public ResponseEntity<TeamDetailPointDTO> getTeamDetailPoint(@PathVariable Long id) {
        log.debug("REST request to get TeamDetailPoint : {}", id);
        Optional<TeamDetailPointDTO> teamDetailPointDTO = teamDetailPointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teamDetailPointDTO);
    }

    /**
     * {@code DELETE  /team-detail-points/:id} : delete the "id" teamDetailPoint.
     *
     * @param id the id of the teamDetailPointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/team-detail-points/{id}")
    public ResponseEntity<Void> deleteTeamDetailPoint(@PathVariable Long id) {
        log.debug("REST request to delete TeamDetailPoint : {}", id);
        teamDetailPointService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
