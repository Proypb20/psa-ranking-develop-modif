package com.ar.pbpoints.service.impl;

import com.ar.pbpoints.service.PlayerPointService;
import com.ar.pbpoints.domain.PlayerPoint;
import com.ar.pbpoints.repository.PlayerPointRepository;
import com.ar.pbpoints.service.dto.PlayerPointDTO;
import com.ar.pbpoints.service.mapper.PlayerPointMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PlayerPoint}.
 */
@Service
@Transactional
public class PlayerPointServiceImpl implements PlayerPointService {

    private final Logger log = LoggerFactory.getLogger(PlayerPointServiceImpl.class);

    private final PlayerPointRepository playerPointRepository;

    private final PlayerPointMapper playerPointMapper;

    public PlayerPointServiceImpl(PlayerPointRepository playerPointRepository, PlayerPointMapper playerPointMapper) {
        this.playerPointRepository = playerPointRepository;
        this.playerPointMapper = playerPointMapper;
    }

    /**
     * Save a playerPoint.
     *
     * @param playerPointDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PlayerPointDTO save(PlayerPointDTO playerPointDTO) {
        log.debug("Request to save PlayerPoint : {}", playerPointDTO);
        PlayerPoint playerPoint = playerPointMapper.toEntity(playerPointDTO);
        playerPoint = playerPointRepository.save(playerPoint);
        return playerPointMapper.toDto(playerPoint);
    }

    /**
     * Get all the playerPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PlayerPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlayerPoints");
        return playerPointRepository.findAll(pageable)
            .map(playerPointMapper::toDto);
    }


    /**
     * Get one playerPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerPointDTO> findOne(Long id) {
        log.debug("Request to get PlayerPoint : {}", id);
        return playerPointRepository.findById(id)
            .map(playerPointMapper::toDto);
    }

    /**
     * Delete the playerPoint by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlayerPoint : {}", id);
        playerPointRepository.deleteById(id);
    }
}
