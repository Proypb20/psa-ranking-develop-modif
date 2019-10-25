package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.Tournament;
import com.psa.ranking.repository.TournamentRepository;
import com.psa.ranking.service.TournamentService;
import com.psa.ranking.service.dto.TournamentDTO;
import com.psa.ranking.service.mapper.TournamentMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.psa.ranking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.psa.ranking.domain.enumeration.Status;
/**
 * Integration tests for the {@link TournamentResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class TournamentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CLOSE_INSCR_DAYS = 1;
    private static final Integer UPDATED_CLOSE_INSCR_DAYS = 2;

    private static final Status DEFAULT_STATUS = Status.CREATED;
    private static final Status UPDATED_STATUS = Status.IN_PROGRESS;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private TournamentService tournamentService;

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

    private MockMvc restTournamentMockMvc;

    private Tournament tournament;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TournamentResource tournamentResource = new TournamentResource(tournamentService);
        this.restTournamentMockMvc = MockMvcBuilders.standaloneSetup(tournamentResource)
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
    public static Tournament createEntity(EntityManager em) {
        Tournament tournament = new Tournament()
            .name(DEFAULT_NAME)
            .closeInscrDays(DEFAULT_CLOSE_INSCR_DAYS)
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return tournament;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tournament createUpdatedEntity(EntityManager em) {
        Tournament tournament = new Tournament()
            .name(UPDATED_NAME)
            .closeInscrDays(UPDATED_CLOSE_INSCR_DAYS)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        return tournament;
    }

    @BeforeEach
    public void initTest() {
        tournament = createEntity(em);
    }

    @Test
    @Transactional
    public void createTournament() throws Exception {
        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);
        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isCreated());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate + 1);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTournament.getCloseInscrDays()).isEqualTo(DEFAULT_CLOSE_INSCR_DAYS);
        assertThat(testTournament.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTournament.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTournament.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createTournamentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tournamentRepository.findAll().size();

        // Create the Tournament with an existing ID
        tournament.setId(1L);
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTournamentMockMvc.perform(post("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTournaments() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get all the tournamentList
        restTournamentMockMvc.perform(get("/api/tournaments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tournament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].closeInscrDays").value(hasItem(DEFAULT_CLOSE_INSCR_DAYS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        // Get the tournament
        restTournamentMockMvc.perform(get("/api/tournaments/{id}", tournament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tournament.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.closeInscrDays").value(DEFAULT_CLOSE_INSCR_DAYS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTournament() throws Exception {
        // Get the tournament
        restTournamentMockMvc.perform(get("/api/tournaments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Update the tournament
        Tournament updatedTournament = tournamentRepository.findById(tournament.getId()).get();
        // Disconnect from session so that the updates on updatedTournament are not directly saved in db
        em.detach(updatedTournament);
        updatedTournament
            .name(UPDATED_NAME)
            .closeInscrDays(UPDATED_CLOSE_INSCR_DAYS)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        TournamentDTO tournamentDTO = tournamentMapper.toDto(updatedTournament);

        restTournamentMockMvc.perform(put("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isOk());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
        Tournament testTournament = tournamentList.get(tournamentList.size() - 1);
        assertThat(testTournament.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTournament.getCloseInscrDays()).isEqualTo(UPDATED_CLOSE_INSCR_DAYS);
        assertThat(testTournament.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTournament.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTournament.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTournament() throws Exception {
        int databaseSizeBeforeUpdate = tournamentRepository.findAll().size();

        // Create the Tournament
        TournamentDTO tournamentDTO = tournamentMapper.toDto(tournament);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTournamentMockMvc.perform(put("/api/tournaments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tournamentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tournament in the database
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTournament() throws Exception {
        // Initialize the database
        tournamentRepository.saveAndFlush(tournament);

        int databaseSizeBeforeDelete = tournamentRepository.findAll().size();

        // Delete the tournament
        restTournamentMockMvc.perform(delete("/api/tournaments/{id}", tournament.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tournament> tournamentList = tournamentRepository.findAll();
        assertThat(tournamentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tournament.class);
        Tournament tournament1 = new Tournament();
        tournament1.setId(1L);
        Tournament tournament2 = new Tournament();
        tournament2.setId(tournament1.getId());
        assertThat(tournament1).isEqualTo(tournament2);
        tournament2.setId(2L);
        assertThat(tournament1).isNotEqualTo(tournament2);
        tournament1.setId(null);
        assertThat(tournament1).isNotEqualTo(tournament2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TournamentDTO.class);
        TournamentDTO tournamentDTO1 = new TournamentDTO();
        tournamentDTO1.setId(1L);
        TournamentDTO tournamentDTO2 = new TournamentDTO();
        assertThat(tournamentDTO1).isNotEqualTo(tournamentDTO2);
        tournamentDTO2.setId(tournamentDTO1.getId());
        assertThat(tournamentDTO1).isEqualTo(tournamentDTO2);
        tournamentDTO2.setId(2L);
        assertThat(tournamentDTO1).isNotEqualTo(tournamentDTO2);
        tournamentDTO1.setId(null);
        assertThat(tournamentDTO1).isNotEqualTo(tournamentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(tournamentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(tournamentMapper.fromId(null)).isNull();
    }
}
