package com.ar.pbpoints.service;

import com.ar.pbpoints.domain.EventCategory;
import com.ar.pbpoints.domain.Game;
import com.ar.pbpoints.domain.Roster;
import com.ar.pbpoints.domain.Team;
import com.ar.pbpoints.domain.enumeration.TimeType;
import com.ar.pbpoints.repository.EventCategoryRepository;
import com.ar.pbpoints.repository.GameRepository;
import com.ar.pbpoints.repository.RosterRepository;
import com.ar.pbpoints.service.dto.EventCategoryDTO;
import com.ar.pbpoints.service.dto.xml.GameResultDTO;
import com.ar.pbpoints.service.mapper.EventCategoryMapper;
import com.ar.pbpoints.service.util.FixtureUtils;
import com.ar.pbpoints.service.util.FixtureUtils.Partido;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

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

    private final GameRepository gameRepository;

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
        // Validaciones de duplicidad
        Optional<EventCategory> optional = eventCategoryRepository.findByEventAndCategory(eventCategory.getEvent(), eventCategory.getCategory());
        if (optional.isPresent()) {
            log.error(optional.get().toString());
            throw new DuplicateKeyException("Ya existe un eventCategory con los datos ingresados");
        }
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
     * @param eventCategory {@link EventCategory}
     */
    public void generarFixture(EventCategory eventCategory) {
        if (eventCategory.getEvent() == null) {
            throw new NoResultException("No hay un evento cargado");
        }
        log.info("*** Generando fixture para el evento {} - categoria {}", eventCategory.getEvent(),
            eventCategory.getCategory());
        List<Roster> rosters = rosterRepository.findByEventCategory(eventCategory);
        log.debug(rosters.toString());
        if (!rosters.isEmpty()) {
            // Creo un set para que se guarden los teams que sean diferentes
            Set<Team> teamsSet = new HashSet<>();
            rosters.forEach(r -> teamsSet.add(r.getTeam()));
            // elimino los viejos games
            eventCategory.getGames().forEach(gameRepository::delete);
            eventCategory.getGames().clear();
            // paso el set a Array, para poder usar mejor las busquedas
            List<Team> teams = new ArrayList<>(teamsSet);
            log.debug("Equipos que disputan el evento-categoria: {}", teams);
            // Desordeno los equipos para modificar el orden de juego siempre
            Collections.shuffle(teams);
            log.debug("Teams desordenados: {}", teams);
            log.debug("Cantidad de equipos: {}", teams.size());
            // Calculo el fixture con los equipos que tengo
            Partido[][] rondas = FixtureUtils.calcularLiga(teams.size());
            FixtureUtils.mostrarPartidos(rondas, Boolean.FALSE);
            log.debug("Mapeando fixture a Games");
            Set<Game> games = new HashSet<>();
            int serie = 1;
            int cantSerie = 0;
            for (int i = 0; i < rondas.length; i++) {
                log.debug("Game " + (i + 1) + ": ");
                for (int j = 0; j < rondas[i].length; j++) {
                    Game game = new Game();
                    /*Agrego Logica de Nro se Series*/
                    if (eventCategory.isSplitDeck()) {
                        cantSerie = cantSerie + 1;
                        if (cantSerie == 3) {
                            cantSerie = 1;
                            serie = serie + 1;
                        }
                        game.setSplitDeckNum(serie);
                    } else {
                        game.setSplitDeckNum(0);
                    }
                    if (eventCategory.getCategory().getGameTimeType() == TimeType.MINUTES)
                        game.setTimeLeft(eventCategory.getCategory().getGameTime() * 60);
                    if (eventCategory.getCategory().getGameTimeType() == TimeType.SECONDS)
                        game.setTimeLeft(eventCategory.getCategory().getGameTime());
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
    }

    public void generarFixture(Long idEventCategory) throws NoResultException {
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

    public Boolean submitXML(MultipartFile file) {
        XmlMapper xmlMapper = new XmlMapper();
        log.info("*** Inicio parseo de fichero con resultados de EventCategory ***");
        try {
            log.info("Fichero: {}", file.getOriginalFilename());
            log.info(file.getBytes().toString());
            GameResultDTO gameResultDTO = xmlMapper.readValue(file.getBytes(), GameResultDTO.class);
            log.debug(gameResultDTO.toString());
        } catch (IOException e) {
            log.error("Error al parsear el fichero: {}", file.getName());
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
