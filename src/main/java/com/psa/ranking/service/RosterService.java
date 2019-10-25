package com.psa.ranking.service;

import com.psa.ranking.domain.Roster;
import com.psa.ranking.repository.RosterRepository;
import com.psa.ranking.service.dto.RosterDTO;
import com.psa.ranking.service.mapper.RosterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Roster}.
 */
@Service
@Transactional
public class RosterService {

    private final Logger log = LoggerFactory.getLogger(RosterService.class);

    private final RosterRepository rosterRepository;

    private final RosterMapper rosterMapper;

    public RosterService(RosterRepository rosterRepository, RosterMapper rosterMapper) {
        this.rosterRepository = rosterRepository;
        this.rosterMapper = rosterMapper;
    }

    /**
     * Save a roster.
     *
     * @param rosterDTO the entity to save.
     * @return the persisted entity.
     */
    public RosterDTO save(RosterDTO rosterDTO) {
        log.debug("Request to save Roster : {}", rosterDTO);
        Roster roster = rosterMapper.toEntity(rosterDTO);
        roster = rosterRepository.save(roster);
        return rosterMapper.toDto(roster);
    }

    /**
     * Get all the rosters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RosterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rosters");
        return rosterRepository.findAll(pageable)
            .map(rosterMapper::toDto);
    }

    /**
     * Get all the rosters with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<RosterDTO> findAllWithEagerRelationships(Pageable pageable) {
        return rosterRepository.findAllWithEagerRelationships(pageable).map(rosterMapper::toDto);
    }
    

    /**
     * Get one roster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RosterDTO> findOne(Long id) {
        log.debug("Request to get Roster : {}", id);
        return rosterRepository.findOneWithEagerRelationships(id)
            .map(rosterMapper::toDto);
    }

    /**
     * Delete the roster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Roster : {}", id);
        rosterRepository.deleteById(id);
    }
}
