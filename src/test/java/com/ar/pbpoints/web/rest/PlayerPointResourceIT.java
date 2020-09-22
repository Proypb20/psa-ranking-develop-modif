package com.ar.pbpoints.web.rest;

import com.ar.pbpoints.PbPointsApp;
import com.ar.pbpoints.domain.PlayerPoint;
import com.ar.pbpoints.domain.Tournament;
import com.ar.pbpoints.domain.User;
import com.ar.pbpoints.domain.Category;
import com.ar.pbpoints.repository.PlayerPointRepository;
import com.ar.pbpoints.service.PlayerPointService;
import com.ar.pbpoints.service.dto.PlayerPointDTO;
import com.ar.pbpoints.service.mapper.PlayerPointMapper;
import com.ar.pbpoints.web.rest.errors.ExceptionTranslator;
import com.ar.pbpoints.service.dto.PlayerPointCriteria;
import com.ar.pbpoints.service.PlayerPointQueryService;

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

import static com.ar.pbpoints.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PlayerPointResource} REST controller.
 */
@SpringBootTest(classes = PbPointsApp.class)
public class PlayerPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    @Autowired
    private PlayerPointRepository playerPointRepository;

    @Autowired
    private PlayerPointMapper playerPointMapper;

    @Autowired
    private PlayerPointService playerPointService;

    @Autowired
    private PlayerPointQueryService playerPointQueryService;

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

    private MockMvc restPlayerPointMockMvc;

    private PlayerPoint playerPoint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerPointResource playerPointResource = new PlayerPointResource(playerPointService, playerPointQueryService);
        this.restPlayerPointMockMvc = MockMvcBuilders.standaloneSetup(playerPointResource)
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
    public static PlayerPoint createEntity(EntityManager em) {
        PlayerPoint playerPoint = new PlayerPoint()
            .points(DEFAULT_POINTS);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        playerPoint.setTournament(tournament);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        playerPoint.setUser(user);
        return playerPoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerPoint createUpdatedEntity(EntityManager em) {
        PlayerPoint playerPoint = new PlayerPoint()
            .points(UPDATED_POINTS);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        playerPoint.setTournament(tournament);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        playerPoint.setUser(user);
        return playerPoint;
    }

    @BeforeEach
    public void initTest() {
        playerPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayerPoint() throws Exception {
        int databaseSizeBeforeCreate = playerPointRepository.findAll().size();

        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);
        restPlayerPointMockMvc.perform(post("/api/player-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerPointDTO)))
            .andExpect(status().isCreated());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerPoint testPlayerPoint = playerPointList.get(playerPointList.size() - 1);
        assertThat(testPlayerPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void createPlayerPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerPointRepository.findAll().size();

        // Create the PlayerPoint with an existing ID
        playerPoint.setId(1L);
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerPointMockMvc.perform(post("/api/player-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerPointRepository.findAll().size();
        // set the field null
        playerPoint.setPoints(null);

        // Create the PlayerPoint, which fails.
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        restPlayerPointMockMvc.perform(post("/api/player-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerPointDTO)))
            .andExpect(status().isBadRequest());

        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlayerPoints() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList
        restPlayerPointMockMvc.perform(get("/api/player-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }

    @Test
    @Transactional
    public void getPlayerPoint() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get the playerPoint
        restPlayerPointMockMvc.perform(get("/api/player-points/{id}", playerPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(playerPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllPlayerPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points equals to DEFAULT_POINTS
        defaultPlayerPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the playerPointList where points equals to UPDATED_POINTS
        defaultPlayerPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points not equals to DEFAULT_POINTS
        defaultPlayerPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the playerPointList where points not equals to UPDATED_POINTS
        defaultPlayerPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultPlayerPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the playerPointList where points equals to UPDATED_POINTS
        defaultPlayerPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is not null
        defaultPlayerPointShouldBeFound("points.specified=true");

        // Get all the playerPointList where points is null
        defaultPlayerPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayerPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is greater than or equal to DEFAULT_POINTS
        defaultPlayerPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerPointList where points is greater than or equal to UPDATED_POINTS
        defaultPlayerPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is less than or equal to DEFAULT_POINTS
        defaultPlayerPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the playerPointList where points is less than or equal to SMALLER_POINTS
        defaultPlayerPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is less than DEFAULT_POINTS
        defaultPlayerPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the playerPointList where points is less than UPDATED_POINTS
        defaultPlayerPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllPlayerPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        // Get all the playerPointList where points is greater than DEFAULT_POINTS
        defaultPlayerPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the playerPointList where points is greater than SMALLER_POINTS
        defaultPlayerPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }


    @Test
    @Transactional
    public void getAllPlayerPointsByTournamentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Tournament tournament = playerPoint.getTournament();
        playerPointRepository.saveAndFlush(playerPoint);
        Long tournamentId = tournament.getId();

        // Get all the playerPointList where tournament equals to tournamentId
        defaultPlayerPointShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the playerPointList where tournament equals to tournamentId + 1
        defaultPlayerPointShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayerPointsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = playerPoint.getUser();
        playerPointRepository.saveAndFlush(playerPoint);
        Long userId = user.getId();

        // Get all the playerPointList where user equals to userId
        defaultPlayerPointShouldBeFound("userId.equals=" + userId);

        // Get all the playerPointList where user equals to userId + 1
        defaultPlayerPointShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayerPointsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        playerPoint.setCategory(category);
        playerPointRepository.saveAndFlush(playerPoint);
        Long categoryId = category.getId();

        // Get all the playerPointList where category equals to categoryId
        defaultPlayerPointShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the playerPointList where category equals to categoryId + 1
        defaultPlayerPointShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlayerPointShouldBeFound(String filter) throws Exception {
        restPlayerPointMockMvc.perform(get("/api/player-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));

        // Check, that the count call also returns 1
        restPlayerPointMockMvc.perform(get("/api/player-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlayerPointShouldNotBeFound(String filter) throws Exception {
        restPlayerPointMockMvc.perform(get("/api/player-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlayerPointMockMvc.perform(get("/api/player-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlayerPoint() throws Exception {
        // Get the playerPoint
        restPlayerPointMockMvc.perform(get("/api/player-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayerPoint() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();

        // Update the playerPoint
        PlayerPoint updatedPlayerPoint = playerPointRepository.findById(playerPoint.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerPoint are not directly saved in db
        em.detach(updatedPlayerPoint);
        updatedPlayerPoint
            .points(UPDATED_POINTS);
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(updatedPlayerPoint);

        restPlayerPointMockMvc.perform(put("/api/player-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerPointDTO)))
            .andExpect(status().isOk());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
        PlayerPoint testPlayerPoint = playerPointList.get(playerPointList.size() - 1);
        assertThat(testPlayerPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayerPoint() throws Exception {
        int databaseSizeBeforeUpdate = playerPointRepository.findAll().size();

        // Create the PlayerPoint
        PlayerPointDTO playerPointDTO = playerPointMapper.toDto(playerPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerPointMockMvc.perform(put("/api/player-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerPoint in the database
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayerPoint() throws Exception {
        // Initialize the database
        playerPointRepository.saveAndFlush(playerPoint);

        int databaseSizeBeforeDelete = playerPointRepository.findAll().size();

        // Delete the playerPoint
        restPlayerPointMockMvc.perform(delete("/api/player-points/{id}", playerPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerPoint> playerPointList = playerPointRepository.findAll();
        assertThat(playerPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerPoint.class);
        PlayerPoint playerPoint1 = new PlayerPoint();
        playerPoint1.setId(1L);
        PlayerPoint playerPoint2 = new PlayerPoint();
        playerPoint2.setId(playerPoint1.getId());
        assertThat(playerPoint1).isEqualTo(playerPoint2);
        playerPoint2.setId(2L);
        assertThat(playerPoint1).isNotEqualTo(playerPoint2);
        playerPoint1.setId(null);
        assertThat(playerPoint1).isNotEqualTo(playerPoint2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerPointDTO.class);
        PlayerPointDTO playerPointDTO1 = new PlayerPointDTO();
        playerPointDTO1.setId(1L);
        PlayerPointDTO playerPointDTO2 = new PlayerPointDTO();
        assertThat(playerPointDTO1).isNotEqualTo(playerPointDTO2);
        playerPointDTO2.setId(playerPointDTO1.getId());
        assertThat(playerPointDTO1).isEqualTo(playerPointDTO2);
        playerPointDTO2.setId(2L);
        assertThat(playerPointDTO1).isNotEqualTo(playerPointDTO2);
        playerPointDTO1.setId(null);
        assertThat(playerPointDTO1).isNotEqualTo(playerPointDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(playerPointMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(playerPointMapper.fromId(null)).isNull();
    }
}
