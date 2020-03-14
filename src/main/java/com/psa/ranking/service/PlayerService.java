package com.psa.ranking.service;

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

import org.hibernate.hql.internal.ast.tree.IsNullLogicOperatorNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository
    		           , PlayerMapper playerMapper
    		           , RosterRepository rosterRepository
    		           , EventCategoryRepository eventCategoryRepository
    		           , TournamentRepository tournamentRepository
    		           , PlayerPointRepository playerPointRepository
    		           , CategoryRepository categoryRepository) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.rosterRepository = rosterRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.tournamentRepository = tournamentRepository;
        this.playerPointRepository = playerPointRepository;
        this.categoryRepository = categoryRepository;
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
	        EventCategory eventCategory = eventCategoryRepository.findByRoster(player.getRoster());
	        /*Obtengo el torneo del eventoCategory*/
	        Tournament tournament = tournamentRepository.findByEvent(eventCategory.getEvent());
	        /*Si el torneo categoriza al jugador*/
	        if (tournament.isCategorize())
	        {
	        	/*Obtengo la categoria a la que pertenece el jugador*/
	            PlayerPoint playerPoint = playerPointRepository.findByUserAndTournament(player.getUser(), tournament);
	            if (playerPoint.getId() == null)
	            {
	            	playerPoint.setPoints((float) 0);
	            	playerPoint.setTournament(tournament);
	            	playerPoint.setUser(player.getUser());
	            	playerPoint.setCategory(categoryRepository.LastCategoryByTournamentId(tournament.getId()));
	            	playerPoint = playerPointRepository.save(playerPoint);
	            }
	            /*Si la categoria del jugador es menor o igual a la del EventoCategoria (orden invertido)*/
	            if (eventCategory.getCategory().getOrder() <= playerPoint.getCategory().getOrder())  
	            {
	               player = playerRepository.save(player);
	               return playerMapper.toDto(player);
	            }
	            else
	            {
	            	/*O el jugador esta en la proxima categoria*/
	                if ((tournament.getCantPlayersNextCategory() > 0 && eventCategory.getCategory().getOrder() == playerPoint.getCategory().getOrder() + 1) 
	                /*Y no hay nadie inscripto*/
	                 && (rosterRepository.CountPlayerNextCategory(player.getRoster().getId(),playerPoint.getCategory().getId()) == tournament.getCantPlayersNextCategory()-1 ))
	                {
	                	player = playerRepository.save(player);
	                    return playerMapper.toDto(player);
	                }
	                else
	                	return playerMapper.toDto(player);
	                	
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
}
