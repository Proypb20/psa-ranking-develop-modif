package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.Format;
import com.psa.ranking.domain.Tournament;
import com.psa.ranking.repository.FormatRepository;
import com.psa.ranking.service.FormatService;
import com.psa.ranking.service.dto.FormatDTO;
import com.psa.ranking.service.mapper.FormatMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;
import com.psa.ranking.service.dto.FormatCriteria;
import com.psa.ranking.service.FormatQueryService;

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
 * Integration tests for the {@link FormatResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class FormatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_COEFICIENT = 1F;
    private static final Float UPDATED_COEFICIENT = 2F;
    private static final Float SMALLER_COEFICIENT = 1F - 1F;

    private static final Integer DEFAULT_PLAYERS_QTY = 1;
    private static final Integer UPDATED_PLAYERS_QTY = 2;
    private static final Integer SMALLER_PLAYERS_QTY = 1 - 1;

    @Autowired
    private FormatRepository formatRepository;

    @Autowired
    private FormatMapper formatMapper;

    @Autowired
    private FormatService formatService;

    @Autowired
    private FormatQueryService formatQueryService;

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

    private MockMvc restFormatMockMvc;

    private Format format;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormatResource formatResource = new FormatResource(formatService, formatQueryService);
        this.restFormatMockMvc = MockMvcBuilders.standaloneSetup(formatResource)
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
    public static Format createEntity(EntityManager em) {
        Format format = new Format()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .coeficient(DEFAULT_COEFICIENT)
            .playersQty(DEFAULT_PLAYERS_QTY);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        format.setTournament(tournament);
        return format;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Format createUpdatedEntity(EntityManager em) {
        Format format = new Format()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .coeficient(UPDATED_COEFICIENT)
            .playersQty(UPDATED_PLAYERS_QTY);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        format.setTournament(tournament);
        return format;
    }

    @BeforeEach
    public void initTest() {
        format = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormat() throws Exception {
        int databaseSizeBeforeCreate = formatRepository.findAll().size();

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);
        restFormatMockMvc.perform(post("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isCreated());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeCreate + 1);
        Format testFormat = formatList.get(formatList.size() - 1);
        assertThat(testFormat.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFormat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormat.getCoeficient()).isEqualTo(DEFAULT_COEFICIENT);
        assertThat(testFormat.getPlayersQty()).isEqualTo(DEFAULT_PLAYERS_QTY);
    }

    @Test
    @Transactional
    public void createFormatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formatRepository.findAll().size();

        // Create the Format with an existing ID
        format.setId(1L);
        FormatDTO formatDTO = formatMapper.toDto(format);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormatMockMvc.perform(post("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = formatRepository.findAll().size();
        // set the field null
        format.setName(null);

        // Create the Format, which fails.
        FormatDTO formatDTO = formatMapper.toDto(format);

        restFormatMockMvc.perform(post("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isBadRequest());

        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCoeficientIsRequired() throws Exception {
        int databaseSizeBeforeTest = formatRepository.findAll().size();
        // set the field null
        format.setCoeficient(null);

        // Create the Format, which fails.
        FormatDTO formatDTO = formatMapper.toDto(format);

        restFormatMockMvc.perform(post("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isBadRequest());

        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormats() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList
        restFormatMockMvc.perform(get("/api/formats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(format.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].coeficient").value(hasItem(DEFAULT_COEFICIENT.doubleValue())))
            .andExpect(jsonPath("$.[*].playersQty").value(hasItem(DEFAULT_PLAYERS_QTY)));
    }
    
    @Test
    @Transactional
    public void getFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get the format
        restFormatMockMvc.perform(get("/api/formats/{id}", format.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(format.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.coeficient").value(DEFAULT_COEFICIENT.doubleValue()))
            .andExpect(jsonPath("$.playersQty").value(DEFAULT_PLAYERS_QTY));
    }

    @Test
    @Transactional
    public void getAllFormatsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name equals to DEFAULT_NAME
        defaultFormatShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the formatList where name equals to UPDATED_NAME
        defaultFormatShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFormatsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name not equals to DEFAULT_NAME
        defaultFormatShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the formatList where name not equals to UPDATED_NAME
        defaultFormatShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFormatsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFormatShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the formatList where name equals to UPDATED_NAME
        defaultFormatShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFormatsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name is not null
        defaultFormatShouldBeFound("name.specified=true");

        // Get all the formatList where name is null
        defaultFormatShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormatsByNameContainsSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name contains DEFAULT_NAME
        defaultFormatShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the formatList where name contains UPDATED_NAME
        defaultFormatShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFormatsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where name does not contain DEFAULT_NAME
        defaultFormatShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the formatList where name does not contain UPDATED_NAME
        defaultFormatShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllFormatsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description equals to DEFAULT_DESCRIPTION
        defaultFormatShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the formatList where description equals to UPDATED_DESCRIPTION
        defaultFormatShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormatsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description not equals to DEFAULT_DESCRIPTION
        defaultFormatShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the formatList where description not equals to UPDATED_DESCRIPTION
        defaultFormatShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormatsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFormatShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the formatList where description equals to UPDATED_DESCRIPTION
        defaultFormatShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormatsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description is not null
        defaultFormatShouldBeFound("description.specified=true");

        // Get all the formatList where description is null
        defaultFormatShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormatsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description contains DEFAULT_DESCRIPTION
        defaultFormatShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the formatList where description contains UPDATED_DESCRIPTION
        defaultFormatShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormatsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where description does not contain DEFAULT_DESCRIPTION
        defaultFormatShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the formatList where description does not contain UPDATED_DESCRIPTION
        defaultFormatShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllFormatsByCoeficientIsEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where coeficient equals to DEFAULT_COEFICIENT
        defaultFormatShouldBeFound("coeficient.equals=" + DEFAULT_COEFICIENT);

        // Get all the formatList where coeficient equals to UPDATED_COEFICIENT
        defaultFormatShouldNotBeFound("coeficient.equals=" + UPDATED_COEFICIENT);
    }

    @Test
    @Transactional
    public void getAllFormatsByCoeficientIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where coeficient not equals to DEFAULT_COEFICIENT
        defaultFormatShouldNotBeFound("coeficient.notEquals=" + DEFAULT_COEFICIENT);

        // Get all the formatList where coeficient not equals to UPDATED_COEFICIENT
        defaultFormatShouldBeFound("coeficient.notEquals=" + UPDATED_COEFICIENT);
    }

    @Test
    @Transactional
    public void getAllFormatsByCoeficientIsInShouldWork() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where coeficient in DEFAULT_COEFICIENT or UPDATED_COEFICIENT
        defaultFormatShouldBeFound("coeficient.in=" + DEFAULT_COEFICIENT + "," + UPDATED_COEFICIENT);

        // Get all the formatList where coeficient equals to UPDATED_COEFICIENT
        defaultFormatShouldNotBeFound("coeficient.in=" + UPDATED_COEFICIENT);
    }

    @Test
    @Transactional
    public void getAllFormatsByCoeficientIsNullOrNotNull() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where coeficient is not null
        defaultFormatShouldBeFound("coeficient.specified=true");

        // Get all the formatList where coeficient is null
        defaultFormatShouldNotBeFound("coeficient.specified=false");
    }

    @Test
    @Transactional
    public void getAllFormatsByCoeficientIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where coeficient is greater than or equal to DEFAULT_COEFICIENT
        defaultFormatShouldBeFound("coeficient.greaterThanOrEqual=" + DEFAULT_COEFICIENT);

        // Get all the formatList where coeficient is greater than or equal to UPDATED_COEFICIENT
        defaultFormatShouldNotBeFound("coeficient.greaterThanOrEqual=" + UPDATED_COEFICIENT);
    }

    @Test
    @Transactional
    public void getAllFormatsByCoeficientIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where coeficient is less than or equal to DEFAULT_COEFICIENT
        defaultFormatShouldBeFound("coeficient.lessThanOrEqual=" + DEFAULT_COEFICIENT);

        // Get all the formatList where coeficient is less than or equal to SMALLER_COEFICIENT
        defaultFormatShouldNotBeFound("coeficient.lessThanOrEqual=" + SMALLER_COEFICIENT);
    }

    @Test
    @Transactional
    public void getAllFormatsByCoeficientIsLessThanSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where coeficient is less than DEFAULT_COEFICIENT
        defaultFormatShouldNotBeFound("coeficient.lessThan=" + DEFAULT_COEFICIENT);

        // Get all the formatList where coeficient is less than UPDATED_COEFICIENT
        defaultFormatShouldBeFound("coeficient.lessThan=" + UPDATED_COEFICIENT);
    }

    @Test
    @Transactional
    public void getAllFormatsByCoeficientIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where coeficient is greater than DEFAULT_COEFICIENT
        defaultFormatShouldNotBeFound("coeficient.greaterThan=" + DEFAULT_COEFICIENT);

        // Get all the formatList where coeficient is greater than SMALLER_COEFICIENT
        defaultFormatShouldBeFound("coeficient.greaterThan=" + SMALLER_COEFICIENT);
    }


    @Test
    @Transactional
    public void getAllFormatsByPlayersQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty equals to DEFAULT_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.equals=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty equals to UPDATED_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.equals=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    public void getAllFormatsByPlayersQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty not equals to DEFAULT_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.notEquals=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty not equals to UPDATED_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.notEquals=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    public void getAllFormatsByPlayersQtyIsInShouldWork() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty in DEFAULT_PLAYERS_QTY or UPDATED_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.in=" + DEFAULT_PLAYERS_QTY + "," + UPDATED_PLAYERS_QTY);

        // Get all the formatList where playersQty equals to UPDATED_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.in=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    public void getAllFormatsByPlayersQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is not null
        defaultFormatShouldBeFound("playersQty.specified=true");

        // Get all the formatList where playersQty is null
        defaultFormatShouldNotBeFound("playersQty.specified=false");
    }

    @Test
    @Transactional
    public void getAllFormatsByPlayersQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is greater than or equal to DEFAULT_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.greaterThanOrEqual=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty is greater than or equal to UPDATED_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.greaterThanOrEqual=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    public void getAllFormatsByPlayersQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is less than or equal to DEFAULT_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.lessThanOrEqual=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty is less than or equal to SMALLER_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.lessThanOrEqual=" + SMALLER_PLAYERS_QTY);
    }

    @Test
    @Transactional
    public void getAllFormatsByPlayersQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is less than DEFAULT_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.lessThan=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty is less than UPDATED_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.lessThan=" + UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    public void getAllFormatsByPlayersQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        // Get all the formatList where playersQty is greater than DEFAULT_PLAYERS_QTY
        defaultFormatShouldNotBeFound("playersQty.greaterThan=" + DEFAULT_PLAYERS_QTY);

        // Get all the formatList where playersQty is greater than SMALLER_PLAYERS_QTY
        defaultFormatShouldBeFound("playersQty.greaterThan=" + SMALLER_PLAYERS_QTY);
    }


    @Test
    @Transactional
    public void getAllFormatsByTournamentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Tournament tournament = format.getTournament();
        formatRepository.saveAndFlush(format);
        Long tournamentId = tournament.getId();

        // Get all the formatList where tournament equals to tournamentId
        defaultFormatShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the formatList where tournament equals to tournamentId + 1
        defaultFormatShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormatShouldBeFound(String filter) throws Exception {
        restFormatMockMvc.perform(get("/api/formats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(format.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].coeficient").value(hasItem(DEFAULT_COEFICIENT.doubleValue())))
            .andExpect(jsonPath("$.[*].playersQty").value(hasItem(DEFAULT_PLAYERS_QTY)));

        // Check, that the count call also returns 1
        restFormatMockMvc.perform(get("/api/formats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormatShouldNotBeFound(String filter) throws Exception {
        restFormatMockMvc.perform(get("/api/formats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormatMockMvc.perform(get("/api/formats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFormat() throws Exception {
        // Get the format
        restFormatMockMvc.perform(get("/api/formats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        int databaseSizeBeforeUpdate = formatRepository.findAll().size();

        // Update the format
        Format updatedFormat = formatRepository.findById(format.getId()).get();
        // Disconnect from session so that the updates on updatedFormat are not directly saved in db
        em.detach(updatedFormat);
        updatedFormat
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .coeficient(UPDATED_COEFICIENT)
            .playersQty(UPDATED_PLAYERS_QTY);
        FormatDTO formatDTO = formatMapper.toDto(updatedFormat);

        restFormatMockMvc.perform(put("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isOk());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
        Format testFormat = formatList.get(formatList.size() - 1);
        assertThat(testFormat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFormat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormat.getCoeficient()).isEqualTo(UPDATED_COEFICIENT);
        assertThat(testFormat.getPlayersQty()).isEqualTo(UPDATED_PLAYERS_QTY);
    }

    @Test
    @Transactional
    public void updateNonExistingFormat() throws Exception {
        int databaseSizeBeforeUpdate = formatRepository.findAll().size();

        // Create the Format
        FormatDTO formatDTO = formatMapper.toDto(format);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormatMockMvc.perform(put("/api/formats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Format in the database
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFormat() throws Exception {
        // Initialize the database
        formatRepository.saveAndFlush(format);

        int databaseSizeBeforeDelete = formatRepository.findAll().size();

        // Delete the format
        restFormatMockMvc.perform(delete("/api/formats/{id}", format.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Format> formatList = formatRepository.findAll();
        assertThat(formatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Format.class);
        Format format1 = new Format();
        format1.setId(1L);
        Format format2 = new Format();
        format2.setId(format1.getId());
        assertThat(format1).isEqualTo(format2);
        format2.setId(2L);
        assertThat(format1).isNotEqualTo(format2);
        format1.setId(null);
        assertThat(format1).isNotEqualTo(format2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormatDTO.class);
        FormatDTO formatDTO1 = new FormatDTO();
        formatDTO1.setId(1L);
        FormatDTO formatDTO2 = new FormatDTO();
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
        formatDTO2.setId(formatDTO1.getId());
        assertThat(formatDTO1).isEqualTo(formatDTO2);
        formatDTO2.setId(2L);
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
        formatDTO1.setId(null);
        assertThat(formatDTO1).isNotEqualTo(formatDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(formatMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(formatMapper.fromId(null)).isNull();
    }
}
