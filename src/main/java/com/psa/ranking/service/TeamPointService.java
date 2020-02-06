package com.psa.ranking.service;

import com.psa.ranking.service.dto.TeamPointDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.psa.ranking.domain.TeamPoint}.
 */
public interface TeamPointService {

    /**
     * Save a teamPoint.
     *
     * @param teamPointDTO the entity to save.
     * @return the persisted entity.
     */
    TeamPointDTO save(TeamPointDTO teamPointDTO);

    /**
     * Get all the teamPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TeamPointDTO> findAll(Pageable pageable);


    /**
     * Get the "id" teamPoint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeamPointDTO> findOne(Long id);

    /**
     * Delete the "id" teamPoint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
