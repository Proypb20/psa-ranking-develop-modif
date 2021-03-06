package com.ar.pbpoints.service;

import java.util.Optional;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ar.pbpoints.domain.EventCategory;
import com.ar.pbpoints.domain.Player;
import com.ar.pbpoints.domain.Roster;
import com.ar.pbpoints.domain.User;
import com.ar.pbpoints.domain.UserExtra;
import com.ar.pbpoints.repository.EventCategoryRepository;
import com.ar.pbpoints.repository.RosterRepository;
import com.ar.pbpoints.repository.UserExtraRepository;
import com.ar.pbpoints.repository.UserRepository;
import com.ar.pbpoints.service.dto.UserExtraDTO;
import com.ar.pbpoints.service.mapper.UserExtraMapper;


/**
 * Service Implementation for managing {@link UserExtra}.
 */
@Service
@Transactional
public class UserExtraService {

    private final Logger log = LoggerFactory.getLogger(UserExtraService.class);

    private final UserExtraRepository userExtraRepository;

    private final UserExtraMapper userExtraMapper;

    private final UserService userService;

    private final EventCategoryRepository eventCategoryRepository;

    private final RosterRepository rosterRepository;

    private final PlayerService playerService;

    private final UserRepository userRepository;

    public UserExtraService(UserExtraRepository userExtraRepository, UserExtraMapper userExtraMapper,
            UserService userService, EventCategoryRepository eventCategoryRepository, RosterRepository rosterRepository,
            PlayerService playerService, UserRepository userRepository) {
        this.userExtraRepository = userExtraRepository;
        this.userExtraMapper = userExtraMapper;
        this.userService = userService;
        this.eventCategoryRepository = eventCategoryRepository;
        this.rosterRepository = rosterRepository;
        this.playerService = playerService;
        this.userRepository = userRepository;
    }

    /**
     * Save a userExtra.
     *
     * @param userExtraDTO the entity to save.
     * @return the persisted entity.
     */
    public UserExtraDTO save(UserExtraDTO userExtraDTO) {
        log.debug("Request to save UserExtra : {}", userExtraDTO);
        UserExtra userExtra = userExtraMapper.toEntity(userExtraDTO);
        long userId = userExtraDTO.getUserId();
        userRepository.findById(userId).ifPresent(userExtra::user);
        userExtra = userExtraRepository.save(userExtra);
        return userExtraMapper.toDto(userExtra);
    }

    /**
     * Get all the userExtras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserExtraDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserExtras");
        return userExtraRepository.findAll(pageable)
            .map(userExtraMapper::toDto);
    }


    /**
     * Get one userExtra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserExtraDTO> findOne(Long id) {
        log.debug("Request to get UserExtra : {}", id);
        return userExtraRepository.findById(id)
            .map(userExtraMapper::toDto);
    }

    /**
     * Delete the userExtra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserExtra : {}", id);
        userExtraRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserExtra> getUserWithAuthorities() {
        User user = Optional.of(userService.getUserWithAuthorities()
                .orElseThrow(() -> new IllegalArgumentException("No hay usuario logueado"))).get();
        Optional<UserExtra> userExtra = userExtraRepository.findById(user.getId());
        /*if (userExtra.isPresent()) {
            userExtra = Optional.of(new UserExtra(user));
        }*/
        log.debug("UserExtra: {} " + userExtra);
        return userExtra;
    }

    /*@Transactional(readOnly = true)
    public Optional<UserExtraDTO> getUniqueUserToRoster(Long idUser, Long idRoster, Long idEventCategory) {
        // Busco si existe el usuario
        UserExtra userExtra = userExtraRepository.findById(idUser)
                .orElseThrow(() -> new NoResultException("No existe un User con ese ID: --> " + idUser));
        log.debug(userExtra.toString());
        // Busco si existe el Roster
        Roster roster = rosterRepository.findById(idRoster)
                .orElseThrow(() -> new NoResultException("No existe Roster con ese ID: --> " + idRoster));
        log.debug(roster.toString());
        // Busco si existe el EventoCategoria
        EventCategory eventCategory = eventCategoryRepository.findById(idEventCategory)
                .orElseThrow(() -> new NoResultException(
                        "No existe un EventoCategory con ese ID: -->" + idEventCategory));
        log.debug(eventCategory.toString());
        log.info("Buscando Player en Roster");
        log.info("Buscando Player en EventCategory");
        // Busco que no exista ese usuario dentro del mismo roster
        Optional<Player> player = playerService.findByUserAndRoster(userExtra.getUser(), roster);
        if (player.isPresent()) {
            throw new IllegalArgumentException("Ya existe el player para el Roster");
        }
        // Busco que no exista ese usuario como jugador en ese EventoCategoria
        player = playerService.findByUserAndEventCategory(userExtra.getUser(), eventCategory);
        if (player.isPresent()) {
            throw new IllegalArgumentException("Ya existe el player para el EventoCategoria");
        }
        return Optional.of(userExtraMapper.toDto(userExtra));
    }*/
}
