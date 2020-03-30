package com.ar.pbpoints.web.rest;

import com.ar.pbpoints.PbPointsApp;
import com.ar.pbpoints.domain.TeamPoint;
import com.ar.pbpoints.domain.Team;
import com.ar.pbpoints.domain.Tournament;
import com.ar.pbpoints.repository.TeamPointRepository;
import com.ar.pbpoints.service.TeamPointService;
import com.ar.pbpoints.service.dto.TeamPointDTO;
import com.ar.pbpoints.service.mapper.TeamPointMapper;
import com.ar.pbpoints.web.rest.errors.ExceptionTranslator;
import com.ar.pbpoints.service.dto.TeamPointCriteria;
import com.ar.pbpoints.service.TeamPointQueryService;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TeamPointResource} REST controller.
 */
@SpringBootTest(classes = PbPointsApp.class)
public class TeamPointResourceIT {

    private static final Float DEFAULT_POINTS = 1F;
    private static final Float UPDATED_POINTS = 2F;
    private static final Float SMALLER_POINTS = 1F - 1F;

    @Autowired
    private TeamPointRepository teamPointRepository;

    @Autowired
    private TeamPointMapper teamPointMapper;

    @Autowired
    private TeamPointService teamPointService;

    @Autowired
    private TeamPointQueryService teamPointQueryService;

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

    private MockMvc restTeamPointMockMvc;

    private TeamPoint teamPoint;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeamPointResource teamPointResource = new TeamPointResource(teamPointService, teamPointQueryService);
        this.restTeamPointMockMvc = MockMvcBuilders.standaloneSetup(teamPointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamPoint createEntity(EntityManager em) {
        TeamPoint teamPoint = new TeamPoint()
            .points(DEFAULT_POINTS);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        teamPoint.setTeam(team);
        return teamPoint;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeamPoint createUpdatedEntity(EntityManager em) {
        TeamPoint teamPoint = new TeamPoint()
            .points(UPDATED_POINTS);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        teamPoint.setTeam(team);
        return teamPoint;
    }

    @BeforeEach
    public void initTest() {
        teamPoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamPoint() throws Exception {
        int databaseSizeBeforeCreate = teamPointRepository.findAll().size();

        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);
        restTeamPointMockMvc.perform(post("/api/team-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isCreated());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeCreate + 1);
        TeamPoint testTeamPoint = teamPointList.get(teamPointList.size() - 1);
        assertThat(testTeamPoint.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void createTeamPointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamPointRepository.findAll().size();

        // Create the TeamPoint with an existing ID
        teamPoint.setId(1L);
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamPointMockMvc.perform(post("/api/team-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamPointRepository.findAll().size();
        // set the field null
        teamPoint.setPoints(null);

        // Create the TeamPoint, which fails.
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        restTeamPointMockMvc.perform(post("/api/team-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isBadRequest());

        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeamPoints() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList
        restTeamPointMockMvc.perform(get("/api/team-points?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));
    }

    @Test
    @Transactional
    public void getTeamPoint() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get the teamPoint
        restTeamPointMockMvc.perform(get("/api/team-points/{id}", teamPoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamPoint.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllTeamPointsByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points equals to DEFAULT_POINTS
        defaultTeamPointShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the teamPointList where points equals to UPDATED_POINTS
        defaultTeamPointShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamPointsByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points not equals to DEFAULT_POINTS
        defaultTeamPointShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the teamPointList where points not equals to UPDATED_POINTS
        defaultTeamPointShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamPointsByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultTeamPointShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the teamPointList where points equals to UPDATED_POINTS
        defaultTeamPointShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamPointsByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is not null
        defaultTeamPointShouldBeFound("points.specified=true");

        // Get all the teamPointList where points is null
        defaultTeamPointShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamPointsByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is greater than or equal to DEFAULT_POINTS
        defaultTeamPointShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamPointList where points is greater than or equal to UPDATED_POINTS
        defaultTeamPointShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamPointsByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is less than or equal to DEFAULT_POINTS
        defaultTeamPointShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the teamPointList where points is less than or equal to SMALLER_POINTS
        defaultTeamPointShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamPointsByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is less than DEFAULT_POINTS
        defaultTeamPointShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the teamPointList where points is less than UPDATED_POINTS
        defaultTeamPointShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllTeamPointsByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        // Get all the teamPointList where points is greater than DEFAULT_POINTS
        defaultTeamPointShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the teamPointList where points is greater than SMALLER_POINTS
        defaultTeamPointShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }


    @Test
    @Transactional
    public void getAllTeamPointsByTeamIsEqualToSomething() throws Exception {
        // Get already existing entity
        Team team = teamPoint.getTeam();
        teamPointRepository.saveAndFlush(teamPoint);
        Long teamId = team.getId();

        // Get all the teamPointList where team equals to teamId
        defaultTeamPointShouldBeFound("teamId.equals=" + teamId);

        // Get all the teamPointList where team equals to teamId + 1
        defaultTeamPointShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }


    @Test
    @Transactional
    public void getAllTeamPointsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        teamPoint.setTournament(tournament);
        teamPointRepository.saveAndFlush(teamPoint);
        Long tournamentId = tournament.getId();

        // Get all the teamPointList where tournament equals to tournamentId
        defaultTeamPointShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the teamPointList where tournament equals to tournamentId + 1
        defaultTeamPointShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeamPointShouldBeFound(String filter) throws Exception {
        restTeamPointMockMvc.perform(get("/api/team-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamPoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.doubleValue())));

        // Check, that the count call also returns 1
        restTeamPointMockMvc.perform(get("/api/team-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeamPointShouldNotBeFound(String filter) throws Exception {
        restTeamPointMockMvc.perform(get("/api/team-points?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeamPointMockMvc.perform(get("/api/team-points/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTeamPoint() throws Exception {
        // Get the teamPoint
        restTeamPointMockMvc.perform(get("/api/team-points/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamPoint() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();

        // Update the teamPoint
        TeamPoint updatedTeamPoint = teamPointRepository.findById(teamPoint.getId()).get();
        // Disconnect from session so that the updates on updatedTeamPoint are not directly saved in db
        em.detach(updatedTeamPoint);
        updatedTeamPoint
            .points(UPDATED_POINTS);
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(updatedTeamPoint);

        restTeamPointMockMvc.perform(put("/api/team-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isOk());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
        TeamPoint testTeamPoint = teamPointList.get(teamPointList.size() - 1);
        assertThat(testTeamPoint.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamPoint() throws Exception {
        int databaseSizeBeforeUpdate = teamPointRepository.findAll().size();

        // Create the TeamPoint
        TeamPointDTO teamPointDTO = teamPointMapper.toDto(teamPoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamPointMockMvc.perform(put("/api/team-points")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamPointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TeamPoint in the database
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeamPoint() throws Exception {
        // Initialize the database
        teamPointRepository.saveAndFlush(teamPoint);

        int databaseSizeBeforeDelete = teamPointRepository.findAll().size();

        // Delete the teamPoint
        restTeamPointMockMvc.perform(delete("/api/team-points/{id}", teamPoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeamPoint> teamPointList = teamPointRepository.findAll();
        assertThat(teamPointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamPoint.class);
        TeamPoint teamPoint1 = new TeamPoint();
        teamPoint1.setId(1L);
        TeamPoint teamPoint2 = new TeamPoint();
        teamPoint2.setId(teamPoint1.getId());
        assertThat(teamPoint1).isEqualTo(teamPoint2);
        teamPoint2.setId(2L);
        assertThat(teamPoint1).isNotEqualTo(teamPoint2);
        teamPoint1.setId(null);
        assertThat(teamPoint1).isNotEqualTo(teamPoint2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeamPointDTO.class);
        TeamPointDTO teamPointDTO1 = new TeamPointDTO();
        teamPointDTO1.setId(1L);
        TeamPointDTO teamPointDTO2 = new TeamPointDTO();
        assertThat(teamPointDTO1).isNotEqualTo(teamPointDTO2);
        teamPointDTO2.setId(teamPointDTO1.getId());
        assertThat(teamPointDTO1).isEqualTo(teamPointDTO2);
        teamPointDTO2.setId(2L);
        assertThat(teamPointDTO1).isNotEqualTo(teamPointDTO2);
        teamPointDTO1.setId(null);
        assertThat(teamPointDTO1).isNotEqualTo(teamPointDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(teamPointMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(teamPointMapper.fromId(null)).isNull();
    }
}
