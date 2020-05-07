package com.ar.pbpoints.service;

import com.ar.pbpoints.domain.Team;
import com.ar.pbpoints.repository.TeamRepository;
import com.ar.pbpoints.service.dto.TeamDTO;
import com.ar.pbpoints.service.mapper.TeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Team}.
 */
@Service
@Transactional
public class TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamService.class);

    private final TeamRepository teamRepository;

    private final TeamMapper teamMapper;

    private final UserService userService;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper, UserService userService) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.userService = userService;
    }

    /**
     * Save a team.
     *
     * @param teamDTO the entity to save.
     * @return the persisted entity.
     */
    public TeamDTO save(TeamDTO teamDTO) {
        log.debug("Request to save Team : {}", teamDTO);
        if (teamDTO.getOwnerId() == null)
        {
          teamDTO.setOwnerId(userService.getUserWithAuthorities().get().getId());
        }
        Team team = teamMapper.toEntity(teamDTO);
        team = teamRepository.save(team);
        return teamMapper.toDto(team);
    }

    /**
     * Get all the teams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TeamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Teams");
        return teamRepository.findAll(pageable)
            .map(teamMapper::toDto);
    }


    /**
     * Get one team by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TeamDTO> findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        return teamRepository.findById(id)
            .map(teamMapper::toDto);
    }

    /**
     * Get one team by Name and Owner.
     *
     * @param name the name of the entity and Owner.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Team> findByNameAndIdOwner(String name, Long id) {
        log.debug("Request to get Team : {}", name);
        return teamRepository.findByNameAndIdOwner(name,id);
    }
    /**
     * Delete the team by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.deleteById(id);
    }
}
