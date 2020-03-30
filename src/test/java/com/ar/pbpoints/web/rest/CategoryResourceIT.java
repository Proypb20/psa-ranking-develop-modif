package com.ar.pbpoints.web.rest;

import com.ar.pbpoints.PbPointsApp;
import com.ar.pbpoints.domain.Category;
import com.ar.pbpoints.domain.Tournament;
import com.ar.pbpoints.repository.CategoryRepository;
import com.ar.pbpoints.service.CategoryService;
import com.ar.pbpoints.service.dto.CategoryDTO;
import com.ar.pbpoints.service.mapper.CategoryMapper;
import com.ar.pbpoints.web.rest.errors.ExceptionTranslator;
import com.ar.pbpoints.service.dto.CategoryCriteria;
import com.ar.pbpoints.service.CategoryQueryService;

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

import com.ar.pbpoints.domain.enumeration.TimeType;
/**
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@SpringBootTest(classes = PbPointsApp.class)
public class CategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final TimeType DEFAULT_GAME_TIME_TYPE = TimeType.MINUTES;
    private static final TimeType UPDATED_GAME_TIME_TYPE = TimeType.SECONDS;

    private static final Integer DEFAULT_GAME_TIME = 1;
    private static final Integer UPDATED_GAME_TIME = 2;
    private static final Integer SMALLER_GAME_TIME = 1 - 1;

    private static final TimeType DEFAULT_STOP_TIME_TYPE = TimeType.MINUTES;
    private static final TimeType UPDATED_STOP_TIME_TYPE = TimeType.SECONDS;

    private static final Integer DEFAULT_STOP_TIME = 1;
    private static final Integer UPDATED_STOP_TIME = 2;
    private static final Integer SMALLER_STOP_TIME = 1 - 1;

    private static final Integer DEFAULT_TOTAL_POINTS = 1;
    private static final Integer UPDATED_TOTAL_POINTS = 2;
    private static final Integer SMALLER_TOTAL_POINTS = 1 - 1;

    private static final Integer DEFAULT_DIF_POINTS = 1;
    private static final Integer UPDATED_DIF_POINTS = 2;
    private static final Integer SMALLER_DIF_POINTS = 1 - 1;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryQueryService categoryQueryService;

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

    private MockMvc restCategoryMockMvc;

    private Category category;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategoryResource categoryResource = new CategoryResource(categoryService, categoryQueryService);
        this.restCategoryMockMvc = MockMvcBuilders.standaloneSetup(categoryResource)
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
    public static Category createEntity(EntityManager em) {
        Category category = new Category()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .gameTimeType(DEFAULT_GAME_TIME_TYPE)
            .gameTime(DEFAULT_GAME_TIME)
            .stopTimeType(DEFAULT_STOP_TIME_TYPE)
            .stopTime(DEFAULT_STOP_TIME)
            .totalPoints(DEFAULT_TOTAL_POINTS)
            .difPoints(DEFAULT_DIF_POINTS)
            .order(DEFAULT_ORDER);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        category.setTournament(tournament);
        return category;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Category createUpdatedEntity(EntityManager em) {
        Category category = new Category()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .gameTimeType(UPDATED_GAME_TIME_TYPE)
            .gameTime(UPDATED_GAME_TIME)
            .stopTimeType(UPDATED_STOP_TIME_TYPE)
            .stopTime(UPDATED_STOP_TIME)
            .totalPoints(UPDATED_TOTAL_POINTS)
            .difPoints(UPDATED_DIF_POINTS)
            .order(UPDATED_ORDER);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        category.setTournament(tournament);
        return category;
    }

    @BeforeEach
    public void initTest() {
        category = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);
        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCategory.getGameTimeType()).isEqualTo(DEFAULT_GAME_TIME_TYPE);
        assertThat(testCategory.getGameTime()).isEqualTo(DEFAULT_GAME_TIME);
        assertThat(testCategory.getStopTimeType()).isEqualTo(DEFAULT_STOP_TIME_TYPE);
        assertThat(testCategory.getStopTime()).isEqualTo(DEFAULT_STOP_TIME);
        assertThat(testCategory.getTotalPoints()).isEqualTo(DEFAULT_TOTAL_POINTS);
        assertThat(testCategory.getDifPoints()).isEqualTo(DEFAULT_DIF_POINTS);
        assertThat(testCategory.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    public void createCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // Create the Category with an existing ID
        category.setId(1L);
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGameTimeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setGameTimeType(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGameTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setGameTime(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStopTimeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setStopTimeType(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStopTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setStopTime(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setTotalPoints(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDifPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setDifPoints(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryRepository.findAll().size();
        // set the field null
        category.setOrder(null);

        // Create the Category, which fails.
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        restCategoryMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].gameTimeType").value(hasItem(DEFAULT_GAME_TIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gameTime").value(hasItem(DEFAULT_GAME_TIME)))
            .andExpect(jsonPath("$.[*].stopTimeType").value(hasItem(DEFAULT_STOP_TIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].stopTime").value(hasItem(DEFAULT_STOP_TIME)))
            .andExpect(jsonPath("$.[*].totalPoints").value(hasItem(DEFAULT_TOTAL_POINTS)))
            .andExpect(jsonPath("$.[*].difPoints").value(hasItem(DEFAULT_DIF_POINTS)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    public void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get the category
        restCategoryMockMvc.perform(get("/api/categories/{id}", category.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(category.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.gameTimeType").value(DEFAULT_GAME_TIME_TYPE.toString()))
            .andExpect(jsonPath("$.gameTime").value(DEFAULT_GAME_TIME))
            .andExpect(jsonPath("$.stopTimeType").value(DEFAULT_STOP_TIME_TYPE.toString()))
            .andExpect(jsonPath("$.stopTime").value(DEFAULT_STOP_TIME))
            .andExpect(jsonPath("$.totalPoints").value(DEFAULT_TOTAL_POINTS))
            .andExpect(jsonPath("$.difPoints").value(DEFAULT_DIF_POINTS))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name equals to DEFAULT_NAME
        defaultCategoryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name not equals to DEFAULT_NAME
        defaultCategoryShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the categoryList where name not equals to UPDATED_NAME
        defaultCategoryShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCategoryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the categoryList where name equals to UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name is not null
        defaultCategoryShouldBeFound("name.specified=true");

        // Get all the categoryList where name is null
        defaultCategoryShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name contains DEFAULT_NAME
        defaultCategoryShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the categoryList where name contains UPDATED_NAME
        defaultCategoryShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where name does not contain DEFAULT_NAME
        defaultCategoryShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the categoryList where name does not contain UPDATED_NAME
        defaultCategoryShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description equals to DEFAULT_DESCRIPTION
        defaultCategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the categoryList where description equals to UPDATED_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description not equals to DEFAULT_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the categoryList where description not equals to UPDATED_DESCRIPTION
        defaultCategoryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the categoryList where description equals to UPDATED_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description is not null
        defaultCategoryShouldBeFound("description.specified=true");

        // Get all the categoryList where description is null
        defaultCategoryShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description contains DEFAULT_DESCRIPTION
        defaultCategoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the categoryList where description contains UPDATED_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where description does not contain DEFAULT_DESCRIPTION
        defaultCategoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the categoryList where description does not contain UPDATED_DESCRIPTION
        defaultCategoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllCategoriesByGameTimeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType equals to DEFAULT_GAME_TIME_TYPE
        defaultCategoryShouldBeFound("gameTimeType.equals=" + DEFAULT_GAME_TIME_TYPE);

        // Get all the categoryList where gameTimeType equals to UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldNotBeFound("gameTimeType.equals=" + UPDATED_GAME_TIME_TYPE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType not equals to DEFAULT_GAME_TIME_TYPE
        defaultCategoryShouldNotBeFound("gameTimeType.notEquals=" + DEFAULT_GAME_TIME_TYPE);

        // Get all the categoryList where gameTimeType not equals to UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldBeFound("gameTimeType.notEquals=" + UPDATED_GAME_TIME_TYPE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType in DEFAULT_GAME_TIME_TYPE or UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldBeFound("gameTimeType.in=" + DEFAULT_GAME_TIME_TYPE + "," + UPDATED_GAME_TIME_TYPE);

        // Get all the categoryList where gameTimeType equals to UPDATED_GAME_TIME_TYPE
        defaultCategoryShouldNotBeFound("gameTimeType.in=" + UPDATED_GAME_TIME_TYPE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTimeType is not null
        defaultCategoryShouldBeFound("gameTimeType.specified=true");

        // Get all the categoryList where gameTimeType is null
        defaultCategoryShouldNotBeFound("gameTimeType.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime equals to DEFAULT_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.equals=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime equals to UPDATED_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.equals=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime not equals to DEFAULT_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.notEquals=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime not equals to UPDATED_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.notEquals=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime in DEFAULT_GAME_TIME or UPDATED_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.in=" + DEFAULT_GAME_TIME + "," + UPDATED_GAME_TIME);

        // Get all the categoryList where gameTime equals to UPDATED_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.in=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is not null
        defaultCategoryShouldBeFound("gameTime.specified=true");

        // Get all the categoryList where gameTime is null
        defaultCategoryShouldNotBeFound("gameTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is greater than or equal to DEFAULT_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.greaterThanOrEqual=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime is greater than or equal to UPDATED_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.greaterThanOrEqual=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is less than or equal to DEFAULT_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.lessThanOrEqual=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime is less than or equal to SMALLER_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.lessThanOrEqual=" + SMALLER_GAME_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is less than DEFAULT_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.lessThan=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime is less than UPDATED_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.lessThan=" + UPDATED_GAME_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByGameTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where gameTime is greater than DEFAULT_GAME_TIME
        defaultCategoryShouldNotBeFound("gameTime.greaterThan=" + DEFAULT_GAME_TIME);

        // Get all the categoryList where gameTime is greater than SMALLER_GAME_TIME
        defaultCategoryShouldBeFound("gameTime.greaterThan=" + SMALLER_GAME_TIME);
    }


    @Test
    @Transactional
    public void getAllCategoriesByStopTimeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType equals to DEFAULT_STOP_TIME_TYPE
        defaultCategoryShouldBeFound("stopTimeType.equals=" + DEFAULT_STOP_TIME_TYPE);

        // Get all the categoryList where stopTimeType equals to UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopTimeType.equals=" + UPDATED_STOP_TIME_TYPE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType not equals to DEFAULT_STOP_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopTimeType.notEquals=" + DEFAULT_STOP_TIME_TYPE);

        // Get all the categoryList where stopTimeType not equals to UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldBeFound("stopTimeType.notEquals=" + UPDATED_STOP_TIME_TYPE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType in DEFAULT_STOP_TIME_TYPE or UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldBeFound("stopTimeType.in=" + DEFAULT_STOP_TIME_TYPE + "," + UPDATED_STOP_TIME_TYPE);

        // Get all the categoryList where stopTimeType equals to UPDATED_STOP_TIME_TYPE
        defaultCategoryShouldNotBeFound("stopTimeType.in=" + UPDATED_STOP_TIME_TYPE);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTimeType is not null
        defaultCategoryShouldBeFound("stopTimeType.specified=true");

        // Get all the categoryList where stopTimeType is null
        defaultCategoryShouldNotBeFound("stopTimeType.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime equals to DEFAULT_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.equals=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime equals to UPDATED_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.equals=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime not equals to DEFAULT_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.notEquals=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime not equals to UPDATED_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.notEquals=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime in DEFAULT_STOP_TIME or UPDATED_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.in=" + DEFAULT_STOP_TIME + "," + UPDATED_STOP_TIME);

        // Get all the categoryList where stopTime equals to UPDATED_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.in=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is not null
        defaultCategoryShouldBeFound("stopTime.specified=true");

        // Get all the categoryList where stopTime is null
        defaultCategoryShouldNotBeFound("stopTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is greater than or equal to DEFAULT_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.greaterThanOrEqual=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime is greater than or equal to UPDATED_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.greaterThanOrEqual=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is less than or equal to DEFAULT_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.lessThanOrEqual=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime is less than or equal to SMALLER_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.lessThanOrEqual=" + SMALLER_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is less than DEFAULT_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.lessThan=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime is less than UPDATED_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.lessThan=" + UPDATED_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllCategoriesByStopTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where stopTime is greater than DEFAULT_STOP_TIME
        defaultCategoryShouldNotBeFound("stopTime.greaterThan=" + DEFAULT_STOP_TIME);

        // Get all the categoryList where stopTime is greater than SMALLER_STOP_TIME
        defaultCategoryShouldBeFound("stopTime.greaterThan=" + SMALLER_STOP_TIME);
    }


    @Test
    @Transactional
    public void getAllCategoriesByTotalPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where totalPoints equals to DEFAULT_TOTAL_POINTS
        defaultCategoryShouldBeFound("totalPoints.equals=" + DEFAULT_TOTAL_POINTS);

        // Get all the categoryList where totalPoints equals to UPDATED_TOTAL_POINTS
        defaultCategoryShouldNotBeFound("totalPoints.equals=" + UPDATED_TOTAL_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTotalPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where totalPoints not equals to DEFAULT_TOTAL_POINTS
        defaultCategoryShouldNotBeFound("totalPoints.notEquals=" + DEFAULT_TOTAL_POINTS);

        // Get all the categoryList where totalPoints not equals to UPDATED_TOTAL_POINTS
        defaultCategoryShouldBeFound("totalPoints.notEquals=" + UPDATED_TOTAL_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTotalPointsIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where totalPoints in DEFAULT_TOTAL_POINTS or UPDATED_TOTAL_POINTS
        defaultCategoryShouldBeFound("totalPoints.in=" + DEFAULT_TOTAL_POINTS + "," + UPDATED_TOTAL_POINTS);

        // Get all the categoryList where totalPoints equals to UPDATED_TOTAL_POINTS
        defaultCategoryShouldNotBeFound("totalPoints.in=" + UPDATED_TOTAL_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTotalPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where totalPoints is not null
        defaultCategoryShouldBeFound("totalPoints.specified=true");

        // Get all the categoryList where totalPoints is null
        defaultCategoryShouldNotBeFound("totalPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByTotalPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where totalPoints is greater than or equal to DEFAULT_TOTAL_POINTS
        defaultCategoryShouldBeFound("totalPoints.greaterThanOrEqual=" + DEFAULT_TOTAL_POINTS);

        // Get all the categoryList where totalPoints is greater than or equal to UPDATED_TOTAL_POINTS
        defaultCategoryShouldNotBeFound("totalPoints.greaterThanOrEqual=" + UPDATED_TOTAL_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTotalPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where totalPoints is less than or equal to DEFAULT_TOTAL_POINTS
        defaultCategoryShouldBeFound("totalPoints.lessThanOrEqual=" + DEFAULT_TOTAL_POINTS);

        // Get all the categoryList where totalPoints is less than or equal to SMALLER_TOTAL_POINTS
        defaultCategoryShouldNotBeFound("totalPoints.lessThanOrEqual=" + SMALLER_TOTAL_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTotalPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where totalPoints is less than DEFAULT_TOTAL_POINTS
        defaultCategoryShouldNotBeFound("totalPoints.lessThan=" + DEFAULT_TOTAL_POINTS);

        // Get all the categoryList where totalPoints is less than UPDATED_TOTAL_POINTS
        defaultCategoryShouldBeFound("totalPoints.lessThan=" + UPDATED_TOTAL_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByTotalPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where totalPoints is greater than DEFAULT_TOTAL_POINTS
        defaultCategoryShouldNotBeFound("totalPoints.greaterThan=" + DEFAULT_TOTAL_POINTS);

        // Get all the categoryList where totalPoints is greater than SMALLER_TOTAL_POINTS
        defaultCategoryShouldBeFound("totalPoints.greaterThan=" + SMALLER_TOTAL_POINTS);
    }


    @Test
    @Transactional
    public void getAllCategoriesByDifPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints equals to DEFAULT_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.equals=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints equals to UPDATED_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.equals=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDifPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints not equals to DEFAULT_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.notEquals=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints not equals to UPDATED_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.notEquals=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDifPointsIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints in DEFAULT_DIF_POINTS or UPDATED_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.in=" + DEFAULT_DIF_POINTS + "," + UPDATED_DIF_POINTS);

        // Get all the categoryList where difPoints equals to UPDATED_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.in=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDifPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is not null
        defaultCategoryShouldBeFound("difPoints.specified=true");

        // Get all the categoryList where difPoints is null
        defaultCategoryShouldNotBeFound("difPoints.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByDifPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is greater than or equal to DEFAULT_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.greaterThanOrEqual=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints is greater than or equal to UPDATED_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.greaterThanOrEqual=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDifPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is less than or equal to DEFAULT_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.lessThanOrEqual=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints is less than or equal to SMALLER_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.lessThanOrEqual=" + SMALLER_DIF_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDifPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is less than DEFAULT_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.lessThan=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints is less than UPDATED_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.lessThan=" + UPDATED_DIF_POINTS);
    }

    @Test
    @Transactional
    public void getAllCategoriesByDifPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where difPoints is greater than DEFAULT_DIF_POINTS
        defaultCategoryShouldNotBeFound("difPoints.greaterThan=" + DEFAULT_DIF_POINTS);

        // Get all the categoryList where difPoints is greater than SMALLER_DIF_POINTS
        defaultCategoryShouldBeFound("difPoints.greaterThan=" + SMALLER_DIF_POINTS);
    }


    @Test
    @Transactional
    public void getAllCategoriesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where order equals to DEFAULT_ORDER
        defaultCategoryShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the categoryList where order equals to UPDATED_ORDER
        defaultCategoryShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllCategoriesByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where order not equals to DEFAULT_ORDER
        defaultCategoryShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the categoryList where order not equals to UPDATED_ORDER
        defaultCategoryShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllCategoriesByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultCategoryShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the categoryList where order equals to UPDATED_ORDER
        defaultCategoryShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllCategoriesByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where order is not null
        defaultCategoryShouldBeFound("order.specified=true");

        // Get all the categoryList where order is null
        defaultCategoryShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    public void getAllCategoriesByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where order is greater than or equal to DEFAULT_ORDER
        defaultCategoryShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the categoryList where order is greater than or equal to UPDATED_ORDER
        defaultCategoryShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllCategoriesByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where order is less than or equal to DEFAULT_ORDER
        defaultCategoryShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the categoryList where order is less than or equal to SMALLER_ORDER
        defaultCategoryShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    public void getAllCategoriesByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where order is less than DEFAULT_ORDER
        defaultCategoryShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the categoryList where order is less than UPDATED_ORDER
        defaultCategoryShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void getAllCategoriesByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        // Get all the categoryList where order is greater than DEFAULT_ORDER
        defaultCategoryShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the categoryList where order is greater than SMALLER_ORDER
        defaultCategoryShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }


    @Test
    @Transactional
    public void getAllCategoriesByTournamentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Tournament tournament = category.getTournament();
        categoryRepository.saveAndFlush(category);
        Long tournamentId = tournament.getId();

        // Get all the categoryList where tournament equals to tournamentId
        defaultCategoryShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the categoryList where tournament equals to tournamentId + 1
        defaultCategoryShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoryShouldBeFound(String filter) throws Exception {
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(category.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].gameTimeType").value(hasItem(DEFAULT_GAME_TIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gameTime").value(hasItem(DEFAULT_GAME_TIME)))
            .andExpect(jsonPath("$.[*].stopTimeType").value(hasItem(DEFAULT_STOP_TIME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].stopTime").value(hasItem(DEFAULT_STOP_TIME)))
            .andExpect(jsonPath("$.[*].totalPoints").value(hasItem(DEFAULT_TOTAL_POINTS)))
            .andExpect(jsonPath("$.[*].difPoints").value(hasItem(DEFAULT_DIF_POINTS)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));

        // Check, that the count call also returns 1
        restCategoryMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoryShouldNotBeFound(String filter) throws Exception {
        restCategoryMockMvc.perform(get("/api/categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoryMockMvc.perform(get("/api/categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        Category updatedCategory = categoryRepository.findById(category.getId()).get();
        // Disconnect from session so that the updates on updatedCategory are not directly saved in db
        em.detach(updatedCategory);
        updatedCategory
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .gameTimeType(UPDATED_GAME_TIME_TYPE)
            .gameTime(UPDATED_GAME_TIME)
            .stopTimeType(UPDATED_STOP_TIME_TYPE)
            .stopTime(UPDATED_STOP_TIME)
            .totalPoints(UPDATED_TOTAL_POINTS)
            .difPoints(UPDATED_DIF_POINTS)
            .order(UPDATED_ORDER);
        CategoryDTO categoryDTO = categoryMapper.toDto(updatedCategory);

        restCategoryMockMvc.perform(put("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        Category testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCategory.getGameTimeType()).isEqualTo(UPDATED_GAME_TIME_TYPE);
        assertThat(testCategory.getGameTime()).isEqualTo(UPDATED_GAME_TIME);
        assertThat(testCategory.getStopTimeType()).isEqualTo(UPDATED_STOP_TIME_TYPE);
        assertThat(testCategory.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
        assertThat(testCategory.getTotalPoints()).isEqualTo(UPDATED_TOTAL_POINTS);
        assertThat(testCategory.getDifPoints()).isEqualTo(UPDATED_DIF_POINTS);
        assertThat(testCategory.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    public void updateNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(category);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc.perform(put("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(category);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Delete the category
        restCategoryMockMvc.perform(delete("/api/categories/{id}", category.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = new Category();
        category1.setId(1L);
        Category category2 = new Category();
        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);
        category2.setId(2L);
        assertThat(category1).isNotEqualTo(category2);
        category1.setId(null);
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryDTO.class);
        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(1L);
        CategoryDTO categoryDTO2 = new CategoryDTO();
        assertThat(categoryDTO1).isNotEqualTo(categoryDTO2);
        categoryDTO2.setId(categoryDTO1.getId());
        assertThat(categoryDTO1).isEqualTo(categoryDTO2);
        categoryDTO2.setId(2L);
        assertThat(categoryDTO1).isNotEqualTo(categoryDTO2);
        categoryDTO1.setId(null);
        assertThat(categoryDTO1).isNotEqualTo(categoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(categoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(categoryMapper.fromId(null)).isNull();
    }
}
