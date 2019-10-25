package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.Roster;
import com.psa.ranking.repository.RosterRepository;
import com.psa.ranking.service.RosterService;
import com.psa.ranking.service.dto.RosterDTO;
import com.psa.ranking.service.mapper.RosterMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.psa.ranking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.psa.ranking.domain.enumeration.ProfileUser;
/**
 * Integration tests for the {@link RosterResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class RosterResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final ProfileUser DEFAULT_PROFILE = ProfileUser.PLAYER;
    private static final ProfileUser UPDATED_PROFILE = ProfileUser.STAFF;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private RosterRepository rosterRepository;

    @Mock
    private RosterRepository rosterRepositoryMock;

    @Autowired
    private RosterMapper rosterMapper;

    @Mock
    private RosterService rosterServiceMock;

    @Autowired
    private RosterService rosterService;

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
        final RosterResource rosterResource = new RosterResource(rosterService);
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
            .active(DEFAULT_ACTIVE)
            .profile(DEFAULT_PROFILE)
            .createDate(DEFAULT_CREATE_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
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
            .active(UPDATED_ACTIVE)
            .profile(UPDATED_PROFILE)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
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
        assertThat(testRoster.getProfile()).isEqualTo(DEFAULT_PROFILE);
        assertThat(testRoster.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testRoster.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
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
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].profile").value(hasItem(DEFAULT_PROFILE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllRostersWithEagerRelationshipsIsEnabled() throws Exception {
        RosterResource rosterResource = new RosterResource(rosterServiceMock);
        when(rosterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restRosterMockMvc = MockMvcBuilders.standaloneSetup(rosterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRosterMockMvc.perform(get("/api/rosters?eagerload=true"))
        .andExpect(status().isOk());

        verify(rosterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllRostersWithEagerRelationshipsIsNotEnabled() throws Exception {
        RosterResource rosterResource = new RosterResource(rosterServiceMock);
            when(rosterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restRosterMockMvc = MockMvcBuilders.standaloneSetup(rosterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restRosterMockMvc.perform(get("/api/rosters?eagerload=true"))
        .andExpect(status().isOk());

            verify(rosterServiceMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.profile").value(DEFAULT_PROFILE.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
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
            .active(UPDATED_ACTIVE)
            .profile(UPDATED_PROFILE)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
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
        assertThat(testRoster.getProfile()).isEqualTo(UPDATED_PROFILE);
        assertThat(testRoster.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testRoster.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
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
