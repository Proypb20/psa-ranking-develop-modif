package com.ar.pbpoints.web.rest;

import com.ar.pbpoints.PbPointsApp;
import com.ar.pbpoints.domain.Bracket;
import com.ar.pbpoints.repository.BracketRepository;
import com.ar.pbpoints.web.rest.errors.ExceptionTranslator;

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
 * Integration tests for the {@link BracketResource} REST controller.
 */
@SpringBootTest(classes = PbPointsApp.class)
public class BracketResourceIT {

    private static final Integer DEFAULT_TEAMS = 1;
    private static final Integer UPDATED_TEAMS = 2;

    private static final Integer DEFAULT_TEAMS_5_A = 1;
    private static final Integer UPDATED_TEAMS_5_A = 2;

    private static final Integer DEFAULT_TEAMS_5_B = 1;
    private static final Integer UPDATED_TEAMS_5_B = 2;

    private static final Integer DEFAULT_TEAMS_6_A = 1;
    private static final Integer UPDATED_TEAMS_6_A = 2;

    private static final Integer DEFAULT_TEAMS_6_B = 1;
    private static final Integer UPDATED_TEAMS_6_B = 2;

    @Autowired
    private BracketRepository bracketRepository;

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

    private MockMvc restBracketMockMvc;

    private Bracket bracket;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BracketResource bracketResource = new BracketResource(bracketRepository);
        this.restBracketMockMvc = MockMvcBuilders.standaloneSetup(bracketResource)
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
    public static Bracket createEntity(EntityManager em) {
        Bracket bracket = new Bracket()
            .teams(DEFAULT_TEAMS)
            .teams5A(DEFAULT_TEAMS_5_A)
            .teams5B(DEFAULT_TEAMS_5_B)
            .teams6A(DEFAULT_TEAMS_6_A)
            .teams6B(DEFAULT_TEAMS_6_B);
        return bracket;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bracket createUpdatedEntity(EntityManager em) {
        Bracket bracket = new Bracket()
            .teams(UPDATED_TEAMS)
            .teams5A(UPDATED_TEAMS_5_A)
            .teams5B(UPDATED_TEAMS_5_B)
            .teams6A(UPDATED_TEAMS_6_A)
            .teams6B(UPDATED_TEAMS_6_B);
        return bracket;
    }

    @BeforeEach
    public void initTest() {
        bracket = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllBrackets() throws Exception {
        // Initialize the database
        bracketRepository.saveAndFlush(bracket);

        // Get all the bracketList
        restBracketMockMvc.perform(get("/api/brackets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bracket.getId().intValue())))
            .andExpect(jsonPath("$.[*].teams").value(hasItem(DEFAULT_TEAMS)))
            .andExpect(jsonPath("$.[*].teams5A").value(hasItem(DEFAULT_TEAMS_5_A)))
            .andExpect(jsonPath("$.[*].teams5B").value(hasItem(DEFAULT_TEAMS_5_B)))
            .andExpect(jsonPath("$.[*].teams6A").value(hasItem(DEFAULT_TEAMS_6_A)))
            .andExpect(jsonPath("$.[*].teams6B").value(hasItem(DEFAULT_TEAMS_6_B)));
    }
    
    @Test
    @Transactional
    public void getBracket() throws Exception {
        // Initialize the database
        bracketRepository.saveAndFlush(bracket);

        // Get the bracket
        restBracketMockMvc.perform(get("/api/brackets/{id}", bracket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bracket.getId().intValue()))
            .andExpect(jsonPath("$.teams").value(DEFAULT_TEAMS))
            .andExpect(jsonPath("$.teams5A").value(DEFAULT_TEAMS_5_A))
            .andExpect(jsonPath("$.teams5B").value(DEFAULT_TEAMS_5_B))
            .andExpect(jsonPath("$.teams6A").value(DEFAULT_TEAMS_6_A))
            .andExpect(jsonPath("$.teams6B").value(DEFAULT_TEAMS_6_B));
    }

    @Test
    @Transactional
    public void getNonExistingBracket() throws Exception {
        // Get the bracket
        restBracketMockMvc.perform(get("/api/brackets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bracket.class);
        Bracket bracket1 = new Bracket();
        bracket1.setId(1L);
        Bracket bracket2 = new Bracket();
        bracket2.setId(bracket1.getId());
        assertThat(bracket1).isEqualTo(bracket2);
        bracket2.setId(2L);
        assertThat(bracket1).isNotEqualTo(bracket2);
        bracket1.setId(null);
        assertThat(bracket1).isNotEqualTo(bracket2);
    }
}
