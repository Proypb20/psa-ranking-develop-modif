package com.psa.ranking.service;

import com.psa.ranking.domain.Tournament;
import com.psa.ranking.domain.User;
import com.psa.ranking.domain.UserExtra;
import com.psa.ranking.repository.TournamentRepository;
import com.psa.ranking.repository.UserExtraRepository;
import com.psa.ranking.repository.UserRepository;
import com.psa.ranking.security.SecurityUtils;
import com.psa.ranking.service.dto.TournamentDTO;
import com.psa.ranking.service.mapper.TournamentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import javax.mail.Session;

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
        Tournament tournament = tournamentMapper.toEntity(tournamentDTO);
        Optional<User> user1 = userService.getUserWithAuthorities();
        UserExtra usere = UserExtraRepository()
        usere.setUser(user1.get());
        log.debug("Request to save Usere : {}", usere);
        tournament.setOwner(usere);
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
