package com.ar.pbpoints.service;

import com.ar.pbpoints.service.dto.TeamDetailPointDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.ar.pbpoints.domain.TeamDetailPoint}.
 */
public interface TeamDetailPointService {

    /**
     * Save a teamDetailPoint.
     *
     * @param teamDetailPointDTO the entity to save.
     * @return the persisted entity.
     */
    TeamDetailPointDTO save(TeamDetailPointDTO teamDetailPointDTO);

    /**
     * Get all the teamDetailPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TeamDetailPointDTO> findAll(Pageable pageable);


    /**
     * Get the "id" teamDetailPoint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeamDetailPointDTO> findOne(Long id);

    /**
     * Delete the "id" teamDetailPoint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
