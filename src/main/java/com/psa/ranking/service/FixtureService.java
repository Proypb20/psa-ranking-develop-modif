package com.psa.ranking.service;

import com.psa.ranking.domain.Fixture;
import com.psa.ranking.repository.FixtureRepository;
import com.psa.ranking.service.dto.FixtureDTO;
import com.psa.ranking.service.mapper.FixtureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Fixture}.
 */
@Service
@Transactional
public class FixtureService {

    private final Logger log = LoggerFactory.getLogger(FixtureService.class);

    private final FixtureRepository fixtureRepository;

    private final FixtureMapper fixtureMapper;

    public FixtureService(FixtureRepository fixtureRepository, FixtureMapper fixtureMapper) {
        this.fixtureRepository = fixtureRepository;
        this.fixtureMapper = fixtureMapper;
    }

    /**
     * Save a fixture.
     *
     * @param fixtureDTO the entity to save.
     * @return the persisted entity.
     */
    public FixtureDTO save(FixtureDTO fixtureDTO) {
        log.debug("Request to save Fixture : {}", fixtureDTO);
        Fixture fixture = fixtureMapper.toEntity(fixtureDTO);
        fixture = fixtureRepository.save(fixture);
        return fixtureMapper.toDto(fixture);
    }

    /**
     * Get all the fixtures.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FixtureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fixtures");
        return fixtureRepository.findAll(pageable)
            .map(fixtureMapper::toDto);
    }


    /**
     * Get one fixture by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FixtureDTO> findOne(Long id) {
        log.debug("Request to get Fixture : {}", id);
        return fixtureRepository.findById(id)
            .map(fixtureMapper::toDto);
    }

    /**
     * Delete the fixture by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Fixture : {}", id);
        fixtureRepository.deleteById(id);
    }
}
