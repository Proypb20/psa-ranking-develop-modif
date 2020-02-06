package com.psa.ranking.service;

import com.psa.ranking.service.dto.PlayerPointDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.psa.ranking.domain.PlayerPoint}.
 */
public interface PlayerPointService {

    /**
     * Save a playerPoint.
     *
     * @param playerPointDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerPointDTO save(PlayerPointDTO playerPointDTO);

    /**
     * Get all the playerPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlayerPointDTO> findAll(Pageable pageable);


    /**
     * Get the "id" playerPoint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerPointDTO> findOne(Long id);

    /**
     * Delete the "id" playerPoint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
