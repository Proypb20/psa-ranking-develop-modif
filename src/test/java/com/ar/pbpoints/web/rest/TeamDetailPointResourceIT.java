package com.ar.pbpoints.web.rest;

import com.ar.pbpoints.PbPointsApp;
import com.ar.pbpoints.domain.TeamDetailPoint;
import com.ar.pbpoints.domain.TeamPoint;
import com.ar.pbpoints.domain.Event;
import com.ar.pbpoints.repository.TeamDetailPointRepository;
import com.ar.pbpoints.service.TeamDetailPointService;
import com.ar.pbpoints.service.dto.TeamDetailPointDTO;
import com.ar.pbpoints.service.mapper.TeamDetailPointMapper;
import com.ar.pbpoints.web.rest.errors.ExceptionTranslator;
import com.ar.pbpoints.service.dto.TeamDetailPointCriteria;
import com.ar.pbpoints.service.TeamDetailPointQueryService;

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
 * Integration tests for the {@link TeamDetailPointResource} REST controller.
 */
@SpringBootTest(classes = PbPointsApp.class)
public class TeamDetailPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    @Autowired
    private TeamDetailPointRepository teamDetailPointRepository;

    @Autowired
    private TeamDetailPointMapper teamDetailPointMapper;

    @Autowired
    private TeamDetailPointService teamDetailPointService;

    @Autowired
    private TeamDetailPointQueryService teamDetailPointQueryService;

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

    private MockMvc restTeamDetailPointMockMvc;

    private TeamDetailPoint teamDetailPoint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamDetailPointResource teamDetailPointResource = new TeamDetailPointResource(teamDetailPointService, teamDetailPointQueryService);
        this.restTeamDetailPointMockMvc = MockMvcBuilders.standaloneSetup(teamDetailPointResource)
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
    public static TeamDetailPoint createEntity(EntityManager em) {
        TeamDetailPoint teamDetailPoint = new TeamDetailPoint()
            .points(DEFAULT_POINTS);
        // Add required entity
        TeamPoint teamPoint;
        if (TestUtil.findAll(em, TeamPoint.class).isEmpty()) {
            teamPoint = TeamPointResourceIT.createEntity(em);
            em.persist(teamPoint);
            em.flush();
        } else {
            teamPoint = TestUtil.findAll(em, TeamPoint.class).get(0);
        }
        teamDetailPoint.setTeamPoint(teamPoint);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        teamDetailPoint.setEvent(event);
        return teamDetailPoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamDetailPoint createUpdatedEntity(EntityManager em) {
        TeamDetailPoint teamDetailPoint = new TeamDetailPoint()
            .points(UPDATED_POINTS);
        // Add required entity
        TeamPoint teamPoint;
        if (TestUtil.findAll(em, TeamPoint.class).isEmpty()) {
            teamPoint = TeamPointResourceIT.createUpdatedEntity(em);
            em.persist(teamPoint);
            em.flush();
        } else {
            teamPoint = TestUtil.findAll(em, TeamPoint.class).get(0);
        }
        teamDetailPoint.setTeamPoint(teamPoint);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        teamDetailPoint.setEvent(event);
        return teamDetailPoint;
    }

    @BeforeEach
    public void initTest() {
        teamDetailPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamDetailPoint() throws Exception {
        int databaseSizeBeforeCreate = teamDetailPointRepository.findAll().size();

        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);
        restTeamDetailPointMockMvc.perform(post("/api/team-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO)))
            .andExpect(status().isCreated());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeCreate + 1);
        TeamDetailPoint testTeamDetailPoint = teamDetailPointList.get(teamDetailPointList.size() - 1);
        assertThat(testTeamDetailPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void createTeamDetailPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamDetailPointRepository.findAll().size();

        // Create the TeamDetailPoint with an existing ID
        teamDetailPoint.setId(1L);
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamDetailPointMockMvc.perform(post("/api/team-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamDetailPointRepository.findAll().size();
        // set the field null
        teamDetailPoint.setPoints(null);

        // Create the TeamDetailPoint, which fails.
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        restTeamDetailPointMockMvc.perform(post("/api/team-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO)))
            .andExpect(status().isBadRequest());

        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeamDetailPoints() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList
        restTeamDetailPointMockMvc.perform(get("/api/team-detail-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamDetailPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }

    @Test
    @Transactional
    public void getTeamDetailPoint() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get the teamDetailPoint
        restTeamDetailPointMockMvc.perform(get("/api/team-detail-points/{id}", teamDetailPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamDetailPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllTeamDetailPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points equals to DEFAULT_POINTS
        defaultTeamDetailPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points equals to UPDATED_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamDetailPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points not equals to DEFAULT_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points not equals to UPDATED_POINTS
        defaultTeamDetailPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamDetailPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultTeamDetailPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the teamDetailPointList where points equals to UPDATED_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamDetailPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is not null
        defaultTeamDetailPointShouldBeFound("points.specified=true");

        // Get all the teamDetailPointList where points is null
        defaultTeamDetailPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamDetailPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is greater than or equal to DEFAULT_POINTS
        defaultTeamDetailPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points is greater than or equal to UPDATED_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamDetailPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is less than or equal to DEFAULT_POINTS
        defaultTeamDetailPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points is less than or equal to SMALLER_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamDetailPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is less than DEFAULT_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points is less than UPDATED_POINTS
        defaultTeamDetailPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamDetailPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        // Get all the teamDetailPointList where points is greater than DEFAULT_POINTS
        defaultTeamDetailPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the teamDetailPointList where points is greater than SMALLER_POINTS
        defaultTeamDetailPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }


    @Test
    @Transactional
    public void getAllTeamDetailPointsByTeamPointIsEqualToSomething() throws Exception {
        // Get already existing entity
        TeamPoint teamPoint = teamDetailPoint.getTeamPoint();
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);
        Long teamPointId = teamPoint.getId();

        // Get all the teamDetailPointList where teamPoint equals to teamPointId
        defaultTeamDetailPointShouldBeFound("teamPointId.equals=" + teamPointId);

        // Get all the teamDetailPointList where teamPoint equals to teamPointId + 1
        defaultTeamDetailPointShouldNotBeFound("teamPointId.equals=" + (teamPointId + 1));
    }


    @Test
    @Transactional
    public void getAllTeamDetailPointsByEventIsEqualToSomething() throws Exception {
        // Get already existing entity
        Event event = teamDetailPoint.getEvent();
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);
        Long eventId = event.getId();

        // Get all the teamDetailPointList where event equals to eventId
        defaultTeamDetailPointShouldBeFound("eventId.equals=" + eventId);

        // Get all the teamDetailPointList where event equals to eventId + 1
        defaultTeamDetailPointShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeamDetailPointShouldBeFound(String filter) throws Exception {
        restTeamDetailPointMockMvc.perform(get("/api/team-detail-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamDetailPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));

        // Check, that the count call also returns 1
        restTeamDetailPointMockMvc.perform(get("/api/team-detail-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeamDetailPointShouldNotBeFound(String filter) throws Exception {
        restTeamDetailPointMockMvc.perform(get("/api/team-detail-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeamDetailPointMockMvc.perform(get("/api/team-detail-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTeamDetailPoint() throws Exception {
        // Get the teamDetailPoint
        restTeamDetailPointMockMvc.perform(get("/api/team-detail-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamDetailPoint() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();

        // Update the teamDetailPoint
        TeamDetailPoint updatedTeamDetailPoint = teamDetailPointRepository.findById(teamDetailPoint.getId()).get();
        // Disconnect from session so that the updates on updatedTeamDetailPoint are not directly saved in db
        em.detach(updatedTeamDetailPoint);
        updatedTeamDetailPoint
            .points(UPDATED_POINTS);
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(updatedTeamDetailPoint);

        restTeamDetailPointMockMvc.perform(put("/api/team-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO)))
            .andExpect(status().isOk());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
        TeamDetailPoint testTeamDetailPoint = teamDetailPointList.get(teamDetailPointList.size() - 1);
        assertThat(testTeamDetailPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamDetailPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamDetailPointRepository.findAll().size();

        // Create the TeamDetailPoint
        TeamDetailPointDTO teamDetailPointDTO = teamDetailPointMapper.toDto(teamDetailPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamDetailPointMockMvc.perform(put("/api/team-detail-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamDetailPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TeamDetailPoint in the database
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeamDetailPoint() throws Exception {
        // Initialize the database
        teamDetailPointRepository.saveAndFlush(teamDetailPoint);

        int databaseSizeBeforeDelete = teamDetailPointRepository.findAll().size();

        // Delete the teamDetailPoint
        restTeamDetailPointMockMvc.perform(delete("/api/team-detail-points/{id}", teamDetailPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeamDetailPoint> teamDetailPointList = teamDetailPointRepository.findAll();
        assertThat(teamDetailPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamDetailPoint.class);
        TeamDetailPoint teamDetailPoint1 = new TeamDetailPoint();
        teamDetailPoint1.setId(1L);
        TeamDetailPoint teamDetailPoint2 = new TeamDetailPoint();
        teamDetailPoint2.setId(teamDetailPoint1.getId());
        assertThat(teamDetailPoint1).isEqualTo(teamDetailPoint2);
        teamDetailPoint2.setId(2L);
        assertThat(teamDetailPoint1).isNotEqualTo(teamDetailPoint2);
        teamDetailPoint1.setId(null);
        assertThat(teamDetailPoint1).isNotEqualTo(teamDetailPoint2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamDetailPointDTO.class);
        TeamDetailPointDTO teamDetailPointDTO1 = new TeamDetailPointDTO();
        teamDetailPointDTO1.setId(1L);
        TeamDetailPointDTO teamDetailPointDTO2 = new TeamDetailPointDTO();
        assertThat(teamDetailPointDTO1).isNotEqualTo(teamDetailPointDTO2);
        teamDetailPointDTO2.setId(teamDetailPointDTO1.getId());
        assertThat(teamDetailPointDTO1).isEqualTo(teamDetailPointDTO2);
        teamDetailPointDTO2.setId(2L);
        assertThat(teamDetailPointDTO1).isNotEqualTo(teamDetailPointDTO2);
        teamDetailPointDTO1.setId(null);
        assertThat(teamDetailPointDTO1).isNotEqualTo(teamDetailPointDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(teamDetailPointMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(teamDetailPointMapper.fromId(null)).isNull();
    }
}
