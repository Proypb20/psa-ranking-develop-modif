package com.psa.ranking.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Player;
import com.psa.ranking.domain.PlayerPoint;
import com.psa.ranking.domain.Roster;
import com.psa.ranking.domain.Tournament;
import com.psa.ranking.domain.User;
import com.psa.ranking.domain.enumeration.ProfileUser;
import com.psa.ranking.repository.CategoryRepository;
import com.psa.ranking.repository.EventCategoryRepository;
import com.psa.ranking.repository.PlayerPointRepository;
import com.psa.ranking.repository.PlayerRepository;
import com.psa.ranking.repository.RosterRepository;
import com.psa.ranking.repository.TournamentRepository;
import com.psa.ranking.repository.UserRepository;
import com.psa.ranking.service.dto.PlayerDTO;
import com.psa.ranking.service.mapper.PlayerMapper;

/**
 * Service Implementation for managing {@link Player}.
 */
@Service
@Transactional
public class PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final RosterRepository rosterRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final TournamentRepository tournamentRepository;
    private final PlayerPointRepository playerPointRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository
    		           , PlayerMapper playerMapper
    		           , RosterRepository rosterRepository
    		           , EventCategoryRepository eventCategoryRepository
    		           , TournamentRepository tournamentRepository
    		           , PlayerPointRepository playerPointRepository
    		           , CategoryRepository categoryRepository
    		           , UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.rosterRepository = rosterRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.tournamentRepository = tournamentRepository;
        this.playerPointRepository = playerPointRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a player.
     *
     * @param playerDTO the entity to save.
     * @return the persisted entity.
     */
    public PlayerDTO save(PlayerDTO playerDTO) {
        log.debug("Request to save Player : {}", playerDTO);
        Player player = playerMapper.toEntity(playerDTO);
        if (player.getProfile().equals(ProfileUser.PLAYER))
        {
	        /*Obtengo el eventoCategory del Roster*/
	        EventCategory eventCategory = eventCategoryRepository.findByRosters(player.getRoster());
	        log.debug("Get EventCategory: {}",eventCategory);
	        /*Obtengo el torneo del eventoCategory*/
	        Tournament tournament = tournamentRepository.findByEvents(eventCategory.getEvent());
	        log.debug("Get Tournament: {}",tournament);
	        /*Si el torneo categoriza al jugador*/
	        if (tournament.isCategorize())
	        {
	        	log.debug("Tournament Categorize");
	        	/*Obtengo la categoria a la que pertenece el jugador*/
	            PlayerPoint playerPoint = playerPointRepository.findByUserAndTournament(player.getUser(), tournament);
	            log.debug("Get PlayerPoint: {}",playerPoint);
	            if (playerPoint == null)
	            {
	            	playerPoint = new PlayerPoint();
	            	playerPoint.setPoints((float) 0);
	            	playerPoint.setTournament(tournament);
	            	playerPoint.setUser(player.getUser());
	            	playerPoint.setCategory(categoryRepository.LastCategoryByTournamentId(tournament.getId()));
	            	log.debug("Get PlayerPoint: {}",playerPoint);
	            	playerPoint = playerPointRepository.save(playerPoint);
	            }
	            /*Si la categoria del jugador es menor o igual a la del EventoCategoria (orden invertido)*/
	            log.debug("Event Category Order: " + eventCategory.getCategory().getOrder());
	            log.debug("Player Category Order: " + playerPoint.getCategory().getOrder());
	            if (eventCategory.getCategory().getOrder() <= playerPoint.getCategory().getOrder())  
	            {
	               player = playerRepository.save(player);
	               return playerMapper.toDto(player);
	            }
	            else
	            {
	            	/*O el jugador esta en la proxima categoria*/
	            	log.debug("Cant Jugadores Proxima Categoria: " + tournament.getCantPlayersNextCategory());
	            	log.debug("Categoria Evento: " + eventCategory.getCategory().getOrder());
	            	log.debug("Categoria Jugador: " + playerPoint.getCategory().getOrder());
	            	log.debug("Cantidad Jugadores Inscriptos: " + rosterRepository.CountPlayerNextCategory(player.getRoster().getId(),playerPoint.getCategory().getId()));
	                if ((tournament.getCantPlayersNextCategory() > 0 && eventCategory.getCategory().getOrder() +1 == playerPoint.getCategory().getOrder()) 
	                /*Y no hay nadie inscripto*/
	                 && (rosterRepository.CountPlayerNextCategory(player.getRoster().getId(),playerPoint.getCategory().getId()) < tournament.getCantPlayersNextCategory()))
	                {
	                	log.debug("Jugador Distinta Categoria Permitido");
	                	player = playerRepository.save(player);
	                    return playerMapper.toDto(player);
	                }
	                else
	                {
	                	log.debug("Error: Jugador Distinta Categoria NO Permitido");
	                	return playerMapper.toDto(player);
	                }
	                	
	            }
	        }
	        else
	        {
	            player = playerRepository.save(player);
	            return playerMapper.toDto(player);
	        }
        }   
        else
        {
            player = playerRepository.save(player);
            return playerMapper.toDto(player);
        }
    }

    /**
     * Get all the players.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlayerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Players");
        return playerRepository.findAll(pageable)
            .map(playerMapper::toDto);
    }


    /**
     * Get one player by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlayerDTO> findOne(Long id) {
        log.debug("Request to get Player : {}", id);
        return playerRepository.findById(id)
            .map(playerMapper::toDto);
    }

    /**
     * Delete the player by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Player : {}", id);
        playerRepository.deleteById(id);
    }
    
    public Optional<Player> findByUserAndEventCategory (User user, EventCategory eventCategory){
        log.debug("Buscando User en EventoCategoria: {}, {}", user, eventCategory);
        for (Roster roster : eventCategory.getRosters()) {
            Optional<Player> player = this.findPlayer(user, roster);
            if (player.isPresent()) {
                return player;
            }
        }
        return Optional.empty();
    }
    
    public Optional<Player> findByUserAndRoster (User user, Roster roster){
        log.debug("Buscando User en Roster: {}, {}", user, roster);
        return this.findPlayer(user, roster);
    }
    
    private Optional<Player> findPlayer (User user, Roster roster) {
        for (Player player : roster.getPlayers()) {
            if (player.getUser().equals(user)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }
    
    public Boolean checkOwner(Long id){
    	Optional<User> user = userRepository.findByRosterId(id);
        if (user.isPresent())
        	return true;
        else
        	return false;
    }
}
