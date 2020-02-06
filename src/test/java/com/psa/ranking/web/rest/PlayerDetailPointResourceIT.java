package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.PlayerDetailPoint;
import com.psa.ranking.domain.Event;
import com.psa.ranking.domain.PlayerPoint;
import com.psa.ranking.repository.PlayerDetailPointRepository;
import com.psa.ranking.service.PlayerDetailPointService;
import com.psa.ranking.service.dto.PlayerDetailPointDTO;
import com.psa.ranking.service.mapper.PlayerDetailPointMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;
import com.psa.ranking.service.dto.PlayerDetailPointCriteria;
import com.psa.ranking.service.PlayerDetailPointQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.psa.ranking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PlayerDetailPointResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class PlayerDetailPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    @Autowired
    private PlayerDetailPointRepository playerDetailPointRepository;

    @Autowired
    private PlayerDetailPointMapper playerDetailPointMapper;

    @Autowired
    private PlayerDetailPointService playerDetailPointService;

    @Autowired
    private PlayerDetailPointQueryService playerDetailPointQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPlayerDetailPointMockMvc;

    private PlayerDetailPoint playerDetailPoint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerDetailPointResource playerDetailPointResource = new PlayerDetailPointResource(playerDetailPointService, playerDetailPointQueryService);
        this.restPlayerDetailPointMockMvc = MockMvcBuilders.standaloneSetup(playerDetailPointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerDetailPoint createEntity(EntityManager em) {
        PlayerDetailPoint playerDetailPoint = new PlayerDetailPoint()
            .points(DEFAULT_POINTS);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        playerDetailPoint.setEvent(event);
        // Add required entity
        PlayerPoint playerPoint;
        if (TestUtil.findAll(em, PlayerPoint.class).isEmpty()) {
            playerPoint = PlayerPointResourceIT.createEntity(em);
            em.persist(playerPoint);
            em.flush();
        } else {
            playerPoint = TestUtil.findAll(em, PlayerPoint.class).get(0);
        }
        playerDetailPoint.setPlayerPoint(playerPoint);
        return playerDetailPoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerDetailPoint createUpdatedEntity(EntityManager em) {
        PlayerDetailPoint playerDetailPoint = new PlayerDetailPoint()
            .points(UPDATED_POINTS);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        playerDetailPoint.setEvent(event);
        // Add required entity
        PlayerPoint playerPoint;
        if (TestUtil.findAll(em, PlayerPoint.class).isEmpty()) {
            playerPoint = PlayerPointResourceIT.createUpdatedEntity(em);
            em.persist(playerPoint);
            em.flush();
        } else {
            playerPoint = TestUtil.findAll(em, PlayerPoint.class).get(0);
        }
        playerDetailPoint.setPlayerPoint(playerPoint);
        return playerDetailPoint;
    }

    @BeforeEach
    public void initTest() {
        playerDetailPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeCreate = playerDetailPointRepository.findAll().size();

        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);
        restPlayerDetailPointMockMvc.perform(post("/api/player-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO)))
            .andExpect(status().isCreated());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerDetailPoint testPlayerDetailPoint = playerDetailPointList.get(playerDetailPointList.size() - 1);
        assertThat(testPlayerDetailPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void createPlayerDetailPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerDetailPointRepository.findAll().size();

        // Create the PlayerDetailPoint with an existing ID
        playerDetailPoint.setId(1L);
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerDetailPointMockMvc.perform(post("/api/player-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerDetailPointRepository.findAll().size();
        // set the field null
        playerDetailPoint.setPoints(null);

        // Create the PlayerDetailPoint, which fails.
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        restPlayerDetailPointMockMvc.perform(post("/api/player-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO)))
            .andExpect(status().isBadRequest());

        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPoints() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList
        restPlayerDetailPointMockMvc.perform(get("/api/player-detail-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerDetailPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getPlayerDetailPoint() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get the playerDetailPoint
        restPlayerDetailPointMockMvc.perform(get("/api/player-detail-points/{id}", playerDetailPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(playerDetailPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points equals to DEFAULT_POINTS
        defaultPlayerDetailPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points equals to UPDATED_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points not equals to DEFAULT_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points not equals to UPDATED_POINTS
        defaultPlayerDetailPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultPlayerDetailPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the playerDetailPointList where points equals to UPDATED_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is not null
        defaultPlayerDetailPointShouldBeFound("points.specified=true");

        // Get all the playerDetailPointList where points is null
        defaultPlayerDetailPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is greater than or equal to DEFAULT_POINTS
        defaultPlayerDetailPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points is greater than or equal to UPDATED_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is less than or equal to DEFAULT_POINTS
        defaultPlayerDetailPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points is less than or equal to SMALLER_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is less than DEFAULT_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points is less than UPDATED_POINTS
        defaultPlayerDetailPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        // Get all the playerDetailPointList where points is greater than DEFAULT_POINTS
        defaultPlayerDetailPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the playerDetailPointList where points is greater than SMALLER_POINTS
        defaultPlayerDetailPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }


    @Test
    @Transactional
    public void getAllPlayerDetailPointsByEventIsEqualToSomething() throws Exception {
        // Get already existing entity
        Event event = playerDetailPoint.getEvent();
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);
        Long eventId = event.getId();

        // Get all the playerDetailPointList where event equals to eventId
        defaultPlayerDetailPointShouldBeFound("eventId.equals=" + eventId);

        // Get all the playerDetailPointList where event equals to eventId + 1
        defaultPlayerDetailPointShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayerDetailPointsByPlayerPointIsEqualToSomething() throws Exception {
        // Get already existing entity
        PlayerPoint playerPoint = playerDetailPoint.getPlayerPoint();
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);
        Long playerPointId = playerPoint.getId();

        // Get all the playerDetailPointList where playerPoint equals to playerPointId
        defaultPlayerDetailPointShouldBeFound("playerPointId.equals=" + playerPointId);

        // Get all the playerDetailPointList where playerPoint equals to playerPointId + 1
        defaultPlayerDetailPointShouldNotBeFound("playerPointId.equals=" + (playerPointId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlayerDetailPointShouldBeFound(String filter) throws Exception {
        restPlayerDetailPointMockMvc.perform(get("/api/player-detail-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerDetailPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));

        // Check, that the count call also returns 1
        restPlayerDetailPointMockMvc.perform(get("/api/player-detail-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlayerDetailPointShouldNotBeFound(String filter) throws Exception {
        restPlayerDetailPointMockMvc.perform(get("/api/player-detail-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlayerDetailPointMockMvc.perform(get("/api/player-detail-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlayerDetailPoint() throws Exception {
        // Get the playerDetailPoint
        restPlayerDetailPointMockMvc.perform(get("/api/player-detail-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayerDetailPoint() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();

        // Update the playerDetailPoint
        PlayerDetailPoint updatedPlayerDetailPoint = playerDetailPointRepository.findById(playerDetailPoint.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerDetailPoint are not directly saved in db
        em.detach(updatedPlayerDetailPoint);
        updatedPlayerDetailPoint
            .points(UPDATED_POINTS);
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(updatedPlayerDetailPoint);

        restPlayerDetailPointMockMvc.perform(put("/api/player-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO)))
            .andExpect(status().isOk());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
        PlayerDetailPoint testPlayerDetailPoint = playerDetailPointList.get(playerDetailPointList.size() - 1);
        assertThat(testPlayerDetailPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayerDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerDetailPointRepository.findAll().size();

        // Create the PlayerDetailPoint
        PlayerDetailPointDTO playerDetailPointDTO = playerDetailPointMapper.toDto(playerDetailPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerDetailPointMockMvc.perform(put("/api/player-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDetailPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerDetailPoint in the database
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayerDetailPoint() throws Exception {
        // Initialize the database
        playerDetailPointRepository.saveAndFlush(playerDetailPoint);

        int databaseSizeBeforeDelete = playerDetailPointRepository.findAll().size();

        // Delete the playerDetailPoint
        restPlayerDetailPointMockMvc.perform(delete("/api/player-detail-points/{id}", playerDetailPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerDetailPoint> playerDetailPointList = playerDetailPointRepository.findAll();
        assertThat(playerDetailPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerDetailPoint.class);
        PlayerDetailPoint playerDetailPoint1 = new PlayerDetailPoint();
        playerDetailPoint1.setId(1L);
        PlayerDetailPoint playerDetailPoint2 = new PlayerDetailPoint();
        playerDetailPoint2.setId(playerDetailPoint1.getId());
        assertThat(playerDetailPoint1).isEqualTo(playerDetailPoint2);
        playerDetailPoint2.setId(2L);
        assertThat(playerDetailPoint1).isNotEqualTo(playerDetailPoint2);
        playerDetailPoint1.setId(null);
        assertThat(playerDetailPoint1).isNotEqualTo(playerDetailPoint2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerDetailPointDTO.class);
        PlayerDetailPointDTO playerDetailPointDTO1 = new PlayerDetailPointDTO();
        playerDetailPointDTO1.setId(1L);
        PlayerDetailPointDTO playerDetailPointDTO2 = new PlayerDetailPointDTO();
        assertThat(playerDetailPointDTO1).isNotEqualTo(playerDetailPointDTO2);
        playerDetailPointDTO2.setId(playerDetailPointDTO1.getId());
        assertThat(playerDetailPointDTO1).isEqualTo(playerDetailPointDTO2);
        playerDetailPointDTO2.setId(2L);
        assertThat(playerDetailPointDTO1).isNotEqualTo(playerDetailPointDTO2);
        playerDetailPointDTO1.setId(null);
        assertThat(playerDetailPointDTO1).isNotEqualTo(playerDetailPointDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(playerDetailPointMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(playerDetailPointMapper.fromId(null)).isNull();
    }
}
