package com.ar.pbpoints.service;

import com.ar.pbpoints.domain.Game;
import com.ar.pbpoints.domain.Team;
import com.ar.pbpoints.domain.enumeration.Status;
import com.ar.pbpoints.repository.GameRepository;
import com.ar.pbpoints.repository.TeamRepository;
import com.ar.pbpoints.service.dto.GameDTO;
import com.ar.pbpoints.service.mapper.GameMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Game}.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    private final GameMapper gameMapper;

    private final TeamRepository teamRepository;

    public GameService(GameRepository gameRepository, GameMapper gameMapper, TeamRepository teamRepository) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
        this.teamRepository = teamRepository;
    }

    /**
     * Save a game.
     *
     * @param gameDTO the entity to save.
     * @return the persisted entity.
     */
    public GameDTO save(GameDTO gameDTO) {
        log.debug("Request to save Game : {}", gameDTO);
        Game game = gameMapper.toEntity(gameDTO);
        game = gameRepository.save(game);
        return gameMapper.toDto(game);
    }

    /**
     * Get all the games.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GameDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Games");
        return gameRepository.findAll(pageable)
            .map(gameMapper::toDto);
    }


    /**
     * Get one game by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GameDTO> findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        return gameRepository.findById(id)
            .map(gameMapper::toDto);
    }

    /**
     * Delete the game by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Game findByXML(com.ar.pbpoints.service.dto.xml.GameDTO gameDTO) {
        log.info("Transformando Game entity");
        // validaciones de entidades
        log.debug("Buscando Game");
        Game game = gameRepository.findById(gameDTO.getId()).orElseThrow(() -> new IllegalArgumentException("No existe un Game con el ID: " + gameDTO.getId()));
        if (!game.getStatus().equals(Status.IN_PROGRESS)) {
            throw new IllegalArgumentException("El game está en estado - " + game.getStatus() + " -, por lo que no se pueden modificar los datos");
        }
        log.debug("Buscando Team A");
        Team teamA = teamRepository.findByName(gameDTO.getTeamA()).orElseThrow(() -> new IllegalArgumentException("No existe el team " + gameDTO.getTeamA()));log.debug("Buscando Team A");
        log.debug("Buscando Team B");
        Team teamB = teamRepository.findByName(gameDTO.getTeamB()).orElseThrow(() -> new IllegalArgumentException("No existe el team " + gameDTO.getTeamB()));
        if (!game.getTeamA().equals(teamA)){
            throw  new IllegalArgumentException ("El Team - " + teamA.getName() + " - no pertenece al Game informado");
        }
        if (!game.getTeamB().equals(teamB)){
            throw  new IllegalArgumentException ("El Team - " + teamB.getName() + " - no pertenece al Game informado");
        }
        // actualizo los datos
        game.setSplitDeckNum(gameDTO.getSplitDeckNum());
        game.setTimeLeft(gameDTO.getTimeLeft());
        game.setClasif(gameDTO.getClasification());
        game.setGroup(gameDTO.getGroup());
        game.setPointsA(gameDTO.getPointsA());
        game.setOvertimeA(gameDTO.getOvertimeA());
        game.setUvuA(gameDTO.getUvuA());
        game.setPointsB(gameDTO.getPointsB());
        game.setOvertimeB(gameDTO.getOvertimeB());
        game.setUvuB(gameDTO.getUvuB());
        game.setStatus(Status.DONE);
        log.debug("Game actualizado: {}", game);
        return game;
    }
}
