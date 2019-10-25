package com.psa.ranking.service;

import com.psa.ranking.domain.UserExtra;
import com.psa.ranking.repository.UserExtraRepository;
import com.psa.ranking.service.dto.UserExtraDTO;
import com.psa.ranking.service.mapper.UserExtraMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserExtra}.
 */
@Service
@Transactional
public class UserExtraService {

    private final Logger log = LoggerFactory.getLogger(UserExtraService.class);

    private final UserExtraRepository userExtraRepository;

    private final UserExtraMapper userExtraMapper;

    public UserExtraService(UserExtraRepository userExtraRepository, UserExtraMapper userExtraMapper) {
        this.userExtraRepository = userExtraRepository;
        this.userExtraMapper = userExtraMapper;
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
}
