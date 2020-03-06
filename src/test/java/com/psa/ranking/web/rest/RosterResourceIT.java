package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.Roster;
import com.psa.ranking.domain.Team;
import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Player;
import com.psa.ranking.repository.RosterRepository;
import com.psa.ranking.service.RosterService;
import com.psa.ranking.service.dto.RosterDTO;
import com.psa.ranking.service.mapper.RosterMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;
import com.psa.ranking.service.dto.RosterCriteria;
import com.psa.ranking.service.RosterQueryService;

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
 * Integration tests for the {@link RosterResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class RosterResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private RosterRepository rosterRepository;

    @Autowired
    private RosterMapper rosterMapper;

    @Autowired
    private RosterService rosterService;

    @Autowired
    private RosterQueryService rosterQueryService;

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

    private MockMvc restRosterMockMvc;

    private Roster roster;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RosterResource rosterResource = new RosterResource(rosterService, rosterQueryService);
        this.restRosterMockMvc = MockMvcBuilders.standaloneSetup(rosterResource)
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
    public static Roster createEntity(EntityManager em) {
        Roster roster = new Roster()
            .active(DEFAULT_ACTIVE);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        roster.setTeam(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        roster.setEventCategory(eventCategory);
        return roster;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roster createUpdatedEntity(EntityManager em) {
        Roster roster = new Roster()
            .active(UPDATED_ACTIVE);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        roster.setTeam(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createUpdatedEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        roster.setEventCategory(eventCategory);
        return roster;
    }

    @BeforeEach
    public void initTest() {
        roster = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoster() throws Exception {
        int databaseSizeBeforeCreate = rosterRepository.findAll().size();

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);
        restRosterMockMvc.perform(post("/api/rosters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isCreated());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeCreate + 1);
        Roster testRoster = rosterList.get(rosterList.size() - 1);
        assertThat(testRoster.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createRosterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rosterRepository.findAll().size();

        // Create the Roster with an existing ID
        roster.setId(1L);
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRosterMockMvc.perform(post("/api/rosters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRosters() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList
        restRosterMockMvc.perform(get("/api/rosters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roster.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get the roster
        restRosterMockMvc.perform(get("/api/rosters/{id}", roster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roster.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllRostersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active equals to DEFAULT_ACTIVE
        defaultRosterShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the rosterList where active equals to UPDATED_ACTIVE
        defaultRosterShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRostersByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active not equals to DEFAULT_ACTIVE
        defaultRosterShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the rosterList where active not equals to UPDATED_ACTIVE
        defaultRosterShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRostersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultRosterShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the rosterList where active equals to UPDATED_ACTIVE
        defaultRosterShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRostersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active is not null
        defaultRosterShouldBeFound("active.specified=true");

        // Get all the rosterList where active is null
        defaultRosterShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllRostersByTeamIsEqualToSomething() throws Exception {
        // Get already existing entity
        Team team = roster.getTeam();
        rosterRepository.saveAndFlush(roster);
        Long teamId = team.getId();

        // Get all the rosterList where team equals to teamId
        defaultRosterShouldBeFound("teamId.equals=" + teamId);

        // Get all the rosterList where team equals to teamId + 1
        defaultRosterShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }


    @Test
    @Transactional
    public void getAllRostersByEventCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        EventCategory eventCategory = roster.getEventCategory();
        rosterRepository.saveAndFlush(roster);
        Long eventCategoryId = eventCategory.getId();

        // Get all the rosterList where eventCategory equals to eventCategoryId
        defaultRosterShouldBeFound("eventCategoryId.equals=" + eventCategoryId);

        // Get all the rosterList where eventCategory equals to eventCategoryId + 1
        defaultRosterShouldNotBeFound("eventCategoryId.equals=" + (eventCategoryId + 1));
    }
    
    
    @Test
    @Transactional
    public void getAllRostersByPlayerIsEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);
        Player player = PlayerResourceIT.createEntity(em);
        em.persist(player);
        em.flush();
        roster.addPlayer(player);
        rosterRepository.saveAndFlush(roster);
        Long playerId = player.getId();

        // Get all the rosterList where player equals to playerId
        defaultRosterShouldBeFound("playerId.equals=" + playerId);

        // Get all the rosterList where player equals to playerId + 1
        defaultRosterShouldNotBeFound("playerId.equals=" + (playerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRosterShouldBeFound(String filter) throws Exception {
        restRosterMockMvc.perform(get("/api/rosters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roster.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restRosterMockMvc.perform(get("/api/rosters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRosterShouldNotBeFound(String filter) throws Exception {
        restRosterMockMvc.perform(get("/api/rosters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRosterMockMvc.perform(get("/api/rosters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRoster() throws Exception {
        // Get the roster
        restRosterMockMvc.perform(get("/api/rosters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();

        // Update the roster
        Roster updatedRoster = rosterRepository.findById(roster.getId()).get();
        // Disconnect from session so that the updates on updatedRoster are not directly saved in db
        em.detach(updatedRoster);
        updatedRoster
            .active(UPDATED_ACTIVE);
        RosterDTO rosterDTO = rosterMapper.toDto(updatedRoster);

        restRosterMockMvc.perform(put("/api/rosters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isOk());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
        Roster testRoster = rosterList.get(rosterList.size() - 1);
        assertThat(testRoster.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingRoster() throws Exception {
        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRosterMockMvc.perform(put("/api/rosters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        int databaseSizeBeforeDelete = rosterRepository.findAll().size();

        // Delete the roster
        restRosterMockMvc.perform(delete("/api/rosters/{id}", roster.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roster.class);
        Roster roster1 = new Roster();
        roster1.setId(1L);
        Roster roster2 = new Roster();
        roster2.setId(roster1.getId());
        assertThat(roster1).isEqualTo(roster2);
        roster2.setId(2L);
        assertThat(roster1).isNotEqualTo(roster2);
        roster1.setId(null);
        assertThat(roster1).isNotEqualTo(roster2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RosterDTO.class);
        RosterDTO rosterDTO1 = new RosterDTO();
        rosterDTO1.setId(1L);
        RosterDTO rosterDTO2 = new RosterDTO();
        assertThat(rosterDTO1).isNotEqualTo(rosterDTO2);
        rosterDTO2.setId(rosterDTO1.getId());
        assertThat(rosterDTO1).isEqualTo(rosterDTO2);
        rosterDTO2.setId(2L);
        assertThat(rosterDTO1).isNotEqualTo(rosterDTO2);
        rosterDTO1.setId(null);
        assertThat(rosterDTO1).isNotEqualTo(rosterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rosterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rosterMapper.fromId(null)).isNull();
    }
}
package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.Roster;
import com.psa.ranking.domain.Team;
import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Player;
import com.psa.ranking.repository.RosterRepository;
import com.psa.ranking.service.RosterService;
import com.psa.ranking.service.dto.RosterDTO;
import com.psa.ranking.service.mapper.RosterMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;
import com.psa.ranking.service.dto.RosterCriteria;
import com.psa.ranking.service.RosterQueryService;

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
 * Integration tests for the {@link RosterResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class RosterResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private RosterRepository rosterRepository;

    @Autowired
    private RosterMapper rosterMapper;

    @Autowired
    private RosterService rosterService;

    @Autowired
    private RosterQueryService rosterQueryService;

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

    private MockMvc restRosterMockMvc;

    private Roster roster;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RosterResource rosterResource = new RosterResource(rosterService, rosterQueryService);
        this.restRosterMockMvc = MockMvcBuilders.standaloneSetup(rosterResource)
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
    public static Roster createEntity(EntityManager em) {
        Roster roster = new Roster()
            .active(DEFAULT_ACTIVE);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        roster.setTeam(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        roster.setEventCategory(eventCategory);
        return roster;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roster createUpdatedEntity(EntityManager em) {
        Roster roster = new Roster()
            .active(UPDATED_ACTIVE);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        roster.setTeam(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createUpdatedEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        roster.setEventCategory(eventCategory);
        return roster;
    }

    @BeforeEach
    public void initTest() {
        roster = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoster() throws Exception {
        int databaseSizeBeforeCreate = rosterRepository.findAll().size();

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);
        restRosterMockMvc.perform(post("/api/rosters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isCreated());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeCreate + 1);
        Roster testRoster = rosterList.get(rosterList.size() - 1);
        assertThat(testRoster.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createRosterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rosterRepository.findAll().size();

        // Create the Roster with an existing ID
        roster.setId(1L);
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRosterMockMvc.perform(post("/api/rosters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRosters() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList
        restRosterMockMvc.perform(get("/api/rosters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roster.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get the roster
        restRosterMockMvc.perform(get("/api/rosters/{id}", roster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roster.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllRostersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active equals to DEFAULT_ACTIVE
        defaultRosterShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the rosterList where active equals to UPDATED_ACTIVE
        defaultRosterShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRostersByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active not equals to DEFAULT_ACTIVE
        defaultRosterShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the rosterList where active not equals to UPDATED_ACTIVE
        defaultRosterShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRostersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultRosterShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the rosterList where active equals to UPDATED_ACTIVE
        defaultRosterShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllRostersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        // Get all the rosterList where active is not null
        defaultRosterShouldBeFound("active.specified=true");

        // Get all the rosterList where active is null
        defaultRosterShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllRostersByTeamIsEqualToSomething() throws Exception {
        // Get already existing entity
        Team team = roster.getTeam();
        rosterRepository.saveAndFlush(roster);
        Long teamId = team.getId();

        // Get all the rosterList where team equals to teamId
        defaultRosterShouldBeFound("teamId.equals=" + teamId);

        // Get all the rosterList where team equals to teamId + 1
        defaultRosterShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }


    @Test
    @Transactional
    public void getAllRostersByEventCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        EventCategory eventCategory = roster.getEventCategory();
        rosterRepository.saveAndFlush(roster);
        Long eventCategoryId = eventCategory.getId();

        // Get all the rosterList where eventCategory equals to eventCategoryId
        defaultRosterShouldBeFound("eventCategoryId.equals=" + eventCategoryId);

        // Get all the rosterList where eventCategory equals to eventCategoryId + 1
        defaultRosterShouldNotBeFound("eventCategoryId.equals=" + (eventCategoryId + 1));
    }


    @Test
    @Transactional
    public void getAllRostersByPlayerIsEqualToSomething() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);
        Player player = PlayerResourceIT.createEntity(em);
        em.persist(player);
        em.flush();
        roster.addPlayer(player);
        rosterRepository.saveAndFlush(roster);
        Long playerId = player.getId();

        // Get all the rosterList where player equals to playerId
        defaultRosterShouldBeFound("playerId.equals=" + playerId);

        // Get all the rosterList where player equals to playerId + 1
        defaultRosterShouldNotBeFound("playerId.equals=" + (playerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRosterShouldBeFound(String filter) throws Exception {
        restRosterMockMvc.perform(get("/api/rosters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roster.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restRosterMockMvc.perform(get("/api/rosters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRosterShouldNotBeFound(String filter) throws Exception {
        restRosterMockMvc.perform(get("/api/rosters?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRosterMockMvc.perform(get("/api/rosters/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingRoster() throws Exception {
        // Get the roster
        restRosterMockMvc.perform(get("/api/rosters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();

        // Update the roster
        Roster updatedRoster = rosterRepository.findById(roster.getId()).get();
        // Disconnect from session so that the updates on updatedRoster are not directly saved in db
        em.detach(updatedRoster);
        updatedRoster
            .active(UPDATED_ACTIVE);
        RosterDTO rosterDTO = rosterMapper.toDto(updatedRoster);

        restRosterMockMvc.perform(put("/api/rosters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isOk());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
        Roster testRoster = rosterList.get(rosterList.size() - 1);
        assertThat(testRoster.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingRoster() throws Exception {
        int databaseSizeBeforeUpdate = rosterRepository.findAll().size();

        // Create the Roster
        RosterDTO rosterDTO = rosterMapper.toDto(roster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRosterMockMvc.perform(put("/api/rosters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rosterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Roster in the database
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoster() throws Exception {
        // Initialize the database
        rosterRepository.saveAndFlush(roster);

        int databaseSizeBeforeDelete = rosterRepository.findAll().size();

        // Delete the roster
        restRosterMockMvc.perform(delete("/api/rosters/{id}", roster.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roster> rosterList = rosterRepository.findAll();
        assertThat(rosterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roster.class);
        Roster roster1 = new Roster();
        roster1.setId(1L);
        Roster roster2 = new Roster();
        roster2.setId(roster1.getId());
        assertThat(roster1).isEqualTo(roster2);
        roster2.setId(2L);
        assertThat(roster1).isNotEqualTo(roster2);
        roster1.setId(null);
        assertThat(roster1).isNotEqualTo(roster2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RosterDTO.class);
        RosterDTO rosterDTO1 = new RosterDTO();
        rosterDTO1.setId(1L);
        RosterDTO rosterDTO2 = new RosterDTO();
        assertThat(rosterDTO1).isNotEqualTo(rosterDTO2);
        rosterDTO2.setId(rosterDTO1.getId());
        assertThat(rosterDTO1).isEqualTo(rosterDTO2);
        rosterDTO2.setId(2L);
        assertThat(rosterDTO1).isNotEqualTo(rosterDTO2);
        rosterDTO1.setId(null);
        assertThat(rosterDTO1).isNotEqualTo(rosterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rosterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rosterMapper.fromId(null)).isNull();
    }
}
