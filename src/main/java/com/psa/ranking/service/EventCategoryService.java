package com.psa.ranking.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Game;
import com.psa.ranking.domain.Roster;
import com.psa.ranking.domain.Team;
import com.psa.ranking.repository.EventCategoryRepository;
import com.psa.ranking.repository.GameRepository;
import com.psa.ranking.repository.RosterRepository;
import com.psa.ranking.service.dto.EventCategoryDTO;
import com.psa.ranking.service.mapper.EventCategoryMapper;
import com.psa.ranking.service.util.FixtureUtils;
import com.psa.ranking.service.util.FixtureUtils.Partido;

/**
 * Service Implementation for managing {@link EventCategory}.
 */
@Service
@Transactional
public class EventCategoryService {

    private final Logger log = LoggerFactory.getLogger(EventCategoryService.class);

    private final EventCategoryRepository eventCategoryRepository;

    private final EventCategoryMapper eventCategoryMapper;

    private final RosterRepository rosterRepository;
    
    private GameRepository gameRepository;

    public EventCategoryService(EventCategoryRepository eventCategoryRepository,
            EventCategoryMapper eventCategoryMapper, RosterRepository rosterRepository, GameRepository gameRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCategoryMapper = eventCategoryMapper;
        this.rosterRepository = rosterRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Save a eventCategory.
     *
     * @param eventCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    public EventCategoryDTO save(EventCategoryDTO eventCategoryDTO) {
        log.debug("Request to save EventCategory : {}", eventCategoryDTO);
        EventCategory eventCategory = eventCategoryMapper.toEntity(eventCategoryDTO);
        eventCategory = eventCategoryRepository.save(eventCategory);
        return eventCategoryMapper.toDto(eventCategory);
    }

    /**
     * Get all the eventCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventCategories");
        return eventCategoryRepository.findAll(pageable).map(eventCategoryMapper::toDto);
    }

    /**
     * Get one eventCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventCategoryDTO> findOne(Long id) {
        log.debug("Request to get EventCategory : {}", id);
        return eventCategoryRepository.findById(id).map(eventCategoryMapper::toDto);
    }

    /**
     * Delete the eventCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventCategory : {}", id);
        eventCategoryRepository.deleteById(id);
    }

    /**
     * A partir de todos los los equipos que van a participar en un
     * evento-categoria, se genera el fixture para generar los games
     * 
     * @param eventCategory
     */
    public void generarFixture(EventCategory eventCategory) {
        if (eventCategory.getEvent() == null) {
            throw new NoResultException("No hay un evento cargado");
        }
        log.info("*** Generando fixture para el evento {} - categoria {}", eventCategory.getEvent(),
                eventCategory.getCategory());
        List<Roster> rosters = rosterRepository.findByEvent(eventCategory.getEvent())
                .orElseThrow(() -> new NoResultException("No hay Rosters para el evento"));
        log.debug(rosters.toString());
        // Creo un set para que se guarden los teams que sean diferentes
        Set<Team> teamsSet = new HashSet<Team>();
        rosters.forEach(r -> teamsSet.add(r.getTeam()));
        // elimino los viejos games
        eventCategory.getGames().forEach(x -> gameRepository.delete(x));
        eventCategory.getGames().clear();
        // paso el set a Array, para poder usar mejor las busquedas
        List<Team> teams = teamsSet.stream().collect(Collectors.toList());
        log.debug("Equipos que disputan el evento-categoria: {}", teams);
        // Desordeno los equipos para modificar el orden de juego siempre
        Collections.shuffle(teams);
        log.debug("Teams desordenados: {}", teams);
        log.debug("Cantidad de equipos: {}", teams.size());
        // Calculo el fixture con los equipos que tengo
        Partido[][] rondas = FixtureUtils.calcularLiga(teams.size());
        FixtureUtils.mostrarPartidos(rondas, Boolean.FALSE);
        log.debug("Mapeando fixture a Games");
        Set<Game> games = new HashSet<Game>();
        for (int i = 0; i < rondas.length; i++) {
            log.debug("Game " + (i + 1) + ": ");
            for (int j = 0; j < rondas[i].length; j++) {
                Game game = new Game();
                game.setTeamA(teams.get((rondas[i][j].local)));
                game.setTeamB(teams.get((rondas[i][j].visitante)));
                log.info("   " + game.getTeamA().toString() + "-" + game.getTeamB().toString());
                game.setEventCategory(eventCategory);
                games.add(game);
            }
        }
        eventCategory.setGames(games);
        eventCategoryRepository.save(eventCategory);
    }

    public void generarFixture(Long idEventCategory) throws NoResultException{
        Optional<EventCategory> eventCategory = eventCategoryRepository.findById(idEventCategory);
        if (eventCategory.isPresent()) {
            this.generarFixture(eventCategory.get());
        } else {
            throw new NoResultException("No se encontró un eventCategory para generar fixture");
        }
    }

    @Scheduled(cron = "${application.cronFixture}")
    public void generarTodosfixture() {
        log.info("*** Inicio de generación automática de fixtures ***");
        Optional<List<EventCategory>> eventCategories = eventCategoryRepository
                .findByEvent_EndInscriptionDate(LocalDate.now());
        if (eventCategories.isPresent()) {
            for (EventCategory eventCategory : eventCategories.get()) {
                this.generarFixture(eventCategory);
            }
        } else {
            log.info("no hay eventos-categorías para el día actual");
        }
        log.info("*** Fin de generación automática de fixtures ***");
    }
}
