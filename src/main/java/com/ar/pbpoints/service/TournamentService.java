package com.ar.pbpoints.service;

import com.ar.pbpoints.domain.Tournament;
import com.ar.pbpoints.repository.TournamentRepository;
import com.ar.pbpoints.service.dto.TournamentDTO;
import com.ar.pbpoints.service.mapper.TournamentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Tournament}.
 */
@Service
@Transactional
public class TournamentService {

    private final Logger log = LoggerFactory.getLogger(TournamentService.class);

    private final TournamentRepository tournamentRepository;

    private final UserService userService;

    private final TournamentMapper tournamentMapper;

    public TournamentService(TournamentRepository tournamentRepository, TournamentMapper tournamentMapper, UserService userService) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentMapper = tournamentMapper;
        this.userService = userService;
    }

    /**
     * Save a tournament.
     *
     * @param tournamentDTO the entity to save.
     * @return the persisted entity.
     */
    public TournamentDTO save(TournamentDTO tournamentDTO) {
        log.debug("Request to save Tournament : {}", tournamentDTO);
        if (tournamentDTO.getOwnerId() == 0)
        {
           tournamentDTO.setOwnerId(userService.getUserWithAuthorities().get().getId());
           log.debug("Request to save Tournament 2: {}", tournamentDTO);
        }
        Tournament tournament = tournamentMapper.toEntity(tournamentDTO);
        tournament = tournamentRepository.save(tournament);
        return tournamentMapper.toDto(tournament);
    }

    /**
     * Get all the tournaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TournamentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tournaments");
        return tournamentRepository.findAll(pageable)
            .map(tournamentMapper::toDto);
    }


    /**
     * Get one tournament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TournamentDTO> findOne(Long id) {
        log.debug("Request to get Tournament : {}", id);
        return tournamentRepository.findById(id)
            .map(tournamentMapper::toDto);
    }

    /**
     * Delete the tournament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tournament : {}", id);
        tournamentRepository.deleteById(id);
    }
}
