package com.ar.pbpoints.service;

import com.ar.pbpoints.domain.Game;
import com.ar.pbpoints.domain.Team;
import com.ar.pbpoints.domain.TeamDetailPoint;
import com.ar.pbpoints.domain.TeamPoint;
import com.ar.pbpoints.domain.enumeration.Status;
import com.ar.pbpoints.repository.GameRepository;
import com.ar.pbpoints.repository.TeamRepository;
import com.ar.pbpoints.repository.TeamPointRepository;
import com.ar.pbpoints.service.dto.GameDTO;
import com.ar.pbpoints.service.dto.xml.PositionDTO;
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

    private final TeamPointRepository teamPointRepository;

    public GameService(GameRepository gameRepository
                     , GameMapper gameMapper
                     , TeamRepository teamRepository
                     , TeamPointRepository teamPointRepository) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
        this.teamRepository = teamRepository;
        this.teamPointRepository = teamPointRepository;
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
    public TeamPoint findPosByXML(com.ar.pbpoints.service.dto.xml.PositionDTO positionDTO) {
        log.info("Transformando TeamPoint entity");
        TeamPoint teamPoint = teamPointRepository.findByTeam(teamRepository.findById(positionDTO.getTeamId()).get());
        log.info("TeamPointId encontrado: {}" + teamPoint.getId());
        if (teamPoint.getId() == null) {
            teamPoint.setTeam(teamRepository.findById(positionDTO.getTeamId()).get());
            teamPoint.setPoints(positionDTO.getPoints());
        }
        else
        {
            teamPoint.setPoints(teamPoint.getPoints() + positionDTO.getPoints());
        }
        return teamPoint;
    }

    @Transactional(readOnly = true)
    public TeamDetailPoint findPosDetByXML(com.ar.pbpoints.service.dto.xml.PositionDTO positionDTO) {
        log.info("Transformando TeamDetailPoint entity");
        TeamDetailPoint teamDetailPoint = new TeamDetailPoint();
        teamDetailPoint.setTeamPoint(teamPointRepository.findByTeam(teamRepository.findById(positionDTO.getTeamId()).get()));
        teamDetailPoint.setPoints(positionDTO.getPoints());
        return teamDetailPoint;
    }

    @Transactional(readOnly = true)
    public Game findByXML(com.ar.pbpoints.service.dto.xml.GameDTO gameDTO) {
        log.info("Transformando Game entity");
        // validaciones de entidades
        log.debug("Buscando Game");
        Game game = new Game();
        try {
            //Game game = gameRepository.findById(gameDTO.getId()).orElseThrow(() -> new IllegalArgumentException("No existe un Game con el ID: " + gameDTO.getId()));
            game = gameRepository.findById(gameDTO.getId()).orElseThrow(() -> new IllegalArgumentException("No existe un Game con el ID: " + gameDTO.getId()));
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
        }
        catch (Exception e)
        {
            log.debug("No se encontro Game");
            log.debug("Buscando Team A");
            Team teamA = teamRepository.findByName(gameDTO.getTeamA()).orElseThrow(() -> new IllegalArgumentException("No existe el team " + gameDTO.getTeamA()));log.debug("Buscando Team A");
            log.debug("Buscando Team B");
            Team teamB = teamRepository.findByName(gameDTO.getTeamB()).orElseThrow(() -> new IllegalArgumentException("No existe el team " + gameDTO.getTeamB()));
        }

        if (!game.getStatus().equals(Status.CREATED)) {
            throw new IllegalArgumentException("El game está en estado - " + game.getStatus() + " -, por lo que no se pueden modificar los datos");
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
