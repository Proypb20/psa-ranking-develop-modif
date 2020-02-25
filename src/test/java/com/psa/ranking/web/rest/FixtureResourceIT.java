package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.Fixture;
import com.psa.ranking.domain.Event;
import com.psa.ranking.repository.FixtureRepository;
import com.psa.ranking.service.FixtureService;
import com.psa.ranking.service.dto.FixtureDTO;
import com.psa.ranking.service.mapper.FixtureMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;
import com.psa.ranking.service.dto.FixtureCriteria;
import com.psa.ranking.service.FixtureQueryService;

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

import com.psa.ranking.domain.enumeration.Status;
/**
 * Integration tests for the {@link FixtureResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class FixtureResourceIT {

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.CREATED;

    @Autowired
    private FixtureRepository fixtureRepository;

    @Autowired
    private FixtureMapper fixtureMapper;

    @Autowired
    private FixtureService fixtureService;

    @Autowired
    private FixtureQueryService fixtureQueryService;

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

    private MockMvc restFixtureMockMvc;

    private Fixture fixture;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FixtureResource fixtureResource = new FixtureResource(fixtureService, fixtureQueryService);
        this.restFixtureMockMvc = MockMvcBuilders.standaloneSetup(fixtureResource)
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
    public static Fixture createEntity(EntityManager em) {
        Fixture fixture = new Fixture()
            .status(DEFAULT_STATUS);
        return fixture;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fixture createUpdatedEntity(EntityManager em) {
        Fixture fixture = new Fixture()
            .status(UPDATED_STATUS);
        return fixture;
    }

    @BeforeEach
    public void initTest() {
        fixture = createEntity(em);
    }

    @Test
    @Transactional
    public void createFixture() throws Exception {
        int databaseSizeBeforeCreate = fixtureRepository.findAll().size();

        // Create the Fixture
        FixtureDTO fixtureDTO = fixtureMapper.toDto(fixture);
        restFixtureMockMvc.perform(post("/api/fixtures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixtureDTO)))
            .andExpect(status().isCreated());

        // Validate the Fixture in the database
        List<Fixture> fixtureList = fixtureRepository.findAll();
        assertThat(fixtureList).hasSize(databaseSizeBeforeCreate + 1);
        Fixture testFixture = fixtureList.get(fixtureList.size() - 1);
        assertThat(testFixture.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createFixtureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fixtureRepository.findAll().size();

        // Create the Fixture with an existing ID
        fixture.setId(1L);
        FixtureDTO fixtureDTO = fixtureMapper.toDto(fixture);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFixtureMockMvc.perform(post("/api/fixtures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixtureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fixture in the database
        List<Fixture> fixtureList = fixtureRepository.findAll();
        assertThat(fixtureList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = fixtureRepository.findAll().size();
        // set the field null
        fixture.setStatus(null);

        // Create the Fixture, which fails.
        FixtureDTO fixtureDTO = fixtureMapper.toDto(fixture);

        restFixtureMockMvc.perform(post("/api/fixtures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixtureDTO)))
            .andExpect(status().isBadRequest());

        List<Fixture> fixtureList = fixtureRepository.findAll();
        assertThat(fixtureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFixtures() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);

        // Get all the fixtureList
        restFixtureMockMvc.perform(get("/api/fixtures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixture.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getFixture() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);

        // Get the fixture
        restFixtureMockMvc.perform(get("/api/fixtures/{id}", fixture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fixture.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllFixturesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);

        // Get all the fixtureList where status equals to DEFAULT_STATUS
        defaultFixtureShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the fixtureList where status equals to UPDATED_STATUS
        defaultFixtureShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFixturesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);

        // Get all the fixtureList where status not equals to DEFAULT_STATUS
        defaultFixtureShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the fixtureList where status not equals to UPDATED_STATUS
        defaultFixtureShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFixturesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);

        // Get all the fixtureList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultFixtureShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the fixtureList where status equals to UPDATED_STATUS
        defaultFixtureShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllFixturesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);

        // Get all the fixtureList where status is not null
        defaultFixtureShouldBeFound("status.specified=true");

        // Get all the fixtureList where status is null
        defaultFixtureShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllFixturesByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);
        Event event = EventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        fixture.setEvent(event);
        fixtureRepository.saveAndFlush(fixture);
        Long eventId = event.getId();

        // Get all the fixtureList where event equals to eventId
        defaultFixtureShouldBeFound("eventId.equals=" + eventId);

        // Get all the fixtureList where event equals to eventId + 1
        defaultFixtureShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFixtureShouldBeFound(String filter) throws Exception {
        restFixtureMockMvc.perform(get("/api/fixtures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fixture.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restFixtureMockMvc.perform(get("/api/fixtures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFixtureShouldNotBeFound(String filter) throws Exception {
        restFixtureMockMvc.perform(get("/api/fixtures?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFixtureMockMvc.perform(get("/api/fixtures/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFixture() throws Exception {
        // Get the fixture
        restFixtureMockMvc.perform(get("/api/fixtures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFixture() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);

        int databaseSizeBeforeUpdate = fixtureRepository.findAll().size();

        // Update the fixture
        Fixture updatedFixture = fixtureRepository.findById(fixture.getId()).get();
        // Disconnect from session so that the updates on updatedFixture are not directly saved in db
        em.detach(updatedFixture);
        updatedFixture
            .status(UPDATED_STATUS);
        FixtureDTO fixtureDTO = fixtureMapper.toDto(updatedFixture);

        restFixtureMockMvc.perform(put("/api/fixtures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixtureDTO)))
            .andExpect(status().isOk());

        // Validate the Fixture in the database
        List<Fixture> fixtureList = fixtureRepository.findAll();
        assertThat(fixtureList).hasSize(databaseSizeBeforeUpdate);
        Fixture testFixture = fixtureList.get(fixtureList.size() - 1);
        assertThat(testFixture.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingFixture() throws Exception {
        int databaseSizeBeforeUpdate = fixtureRepository.findAll().size();

        // Create the Fixture
        FixtureDTO fixtureDTO = fixtureMapper.toDto(fixture);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFixtureMockMvc.perform(put("/api/fixtures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fixtureDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fixture in the database
        List<Fixture> fixtureList = fixtureRepository.findAll();
        assertThat(fixtureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFixture() throws Exception {
        // Initialize the database
        fixtureRepository.saveAndFlush(fixture);

        int databaseSizeBeforeDelete = fixtureRepository.findAll().size();

        // Delete the fixture
        restFixtureMockMvc.perform(delete("/api/fixtures/{id}", fixture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fixture> fixtureList = fixtureRepository.findAll();
        assertThat(fixtureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fixture.class);
        Fixture fixture1 = new Fixture();
        fixture1.setId(1L);
        Fixture fixture2 = new Fixture();
        fixture2.setId(fixture1.getId());
        assertThat(fixture1).isEqualTo(fixture2);
        fixture2.setId(2L);
        assertThat(fixture1).isNotEqualTo(fixture2);
        fixture1.setId(null);
        assertThat(fixture1).isNotEqualTo(fixture2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FixtureDTO.class);
        FixtureDTO fixtureDTO1 = new FixtureDTO();
        fixtureDTO1.setId(1L);
        FixtureDTO fixtureDTO2 = new FixtureDTO();
        assertThat(fixtureDTO1).isNotEqualTo(fixtureDTO2);
        fixtureDTO2.setId(fixtureDTO1.getId());
        assertThat(fixtureDTO1).isEqualTo(fixtureDTO2);
        fixtureDTO2.setId(2L);
        assertThat(fixtureDTO1).isNotEqualTo(fixtureDTO2);
        fixtureDTO1.setId(null);
        assertThat(fixtureDTO1).isNotEqualTo(fixtureDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(fixtureMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(fixtureMapper.fromId(null)).isNull();
    }
}
