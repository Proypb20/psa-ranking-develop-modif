package com.psa.ranking.service;

import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Player;
import com.psa.ranking.domain.Roster;
import com.psa.ranking.domain.User;
import com.psa.ranking.repository.PlayerRepository;
import com.psa.ranking.service.dto.PlayerDTO;
import com.psa.ranking.service.mapper.PlayerMapper;
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

    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
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
        player = playerRepository.save(player);
        return playerMapper.toDto(player);
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
