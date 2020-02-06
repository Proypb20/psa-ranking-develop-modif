package com.psa.ranking.service.impl;

import com.psa.ranking.service.PlayerDetailPointService;
import com.psa.ranking.domain.PlayerDetailPoint;
import com.psa.ranking.repository.PlayerDetailPointRepository;
import com.psa.ranking.service.dto.PlayerDetailPointDTO;
import com.psa.ranking.service.mapper.PlayerDetailPointMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PlayerDetailPoint}.
 */
@Service
@Transactional
public class PlayerDetailPointServiceImpl implements PlayerDetailPointService {

    private final Logger log = LoggerFactory.getLogger(PlayerDetailPointServiceImpl.class);

    private final PlayerDetailPointRepository playerDetailPointRepository;

    private final PlayerDetailPointMapper playerDetailPointMapper;

    public PlayerDetailPointServiceImpl(PlayerDetailPointRepository playerDetailPointRepository, PlayerDetailPointMapper playerDetailPointMapper) {
        this.playerDetailPointRepository = playerDetailPointRepository;
        this.playerDetailPointMapper = playerDetailPointMapper;
    }

    /**
     * Save a playerDetailPoint.
     *
     * @param playerDetailPointDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PlayerDetailPointDTO save(PlayerDetailPointDTO playerDetailPointDTO) {
        log.debug("Request to save PlayerDetailPoint : {}", playerDetailPointDTO);
        PlayerDetailPoint playerDetailPoint = playerDetailPointMapper.toEntity(playerDetailPointDTO);
        playerDetailPoint = playerDetailPointRepository.save(playerDetailPoint);
        return playerDetailPointMapper.toDto(playerDetailPoint);
    }

    /**
     * Get all the playerDetailPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PlayerDetailPointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlayerDetailPoints");
        return playerDetailPointRepository.findAll(pageable)
            .map(playerDetailPointMapper::toDto);
    }


    /**
     * Get one playerDetailPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerDetailPointDTO> findOne(Long id) {
        log.debug("Request to get PlayerDetailPoint : {}", id);
        return playerDetailPointRepository.findById(id)
            .map(playerDetailPointMapper::toDto);
    }

    /**
     * Delete the playerDetailPoint by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlayerDetailPoint : {}", id);
        playerDetailPointRepository.deleteById(id);
    }
}
