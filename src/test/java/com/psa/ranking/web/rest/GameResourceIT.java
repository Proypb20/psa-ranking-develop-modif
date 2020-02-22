package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.Game;
import com.psa.ranking.domain.Fixture;
import com.psa.ranking.domain.Team;
import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.repository.GameRepository;
import com.psa.ranking.service.GameService;
import com.psa.ranking.service.dto.GameDTO;
import com.psa.ranking.service.mapper.GameMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;
import com.psa.ranking.service.dto.GameCriteria;
import com.psa.ranking.service.GameQueryService;

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
 * Integration tests for the {@link GameResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class GameResourceIT {

    private static final Integer DEFAULT_POINTS_A = 1;
    private static final Integer UPDATED_POINTS_A = 2;
    private static final Integer SMALLER_POINTS_A = 1 - 1;

    private static final Integer DEFAULT_POINTS_B = 1;
    private static final Integer UPDATED_POINTS_B = 2;
    private static final Integer SMALLER_POINTS_B = 1 - 1;

    private static final Integer DEFAULT_SPLIT_DECK_NUM = 1;
    private static final Integer UPDATED_SPLIT_DECK_NUM = 2;
    private static final Integer SMALLER_SPLIT_DECK_NUM = 1 - 1;

    private static final Integer DEFAULT_TIME_LEFT = 1;
    private static final Integer UPDATED_TIME_LEFT = 2;
    private static final Integer SMALLER_TIME_LEFT = 1 - 1;

    private static final Status DEFAULT_STATUS = Status.PENDING;
    private static final Status UPDATED_STATUS = Status.CREATED;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameQueryService gameQueryService;

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

    private MockMvc restGameMockMvc;

    private Game game;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GameResource gameResource = new GameResource(gameService, gameQueryService);
        this.restGameMockMvc = MockMvcBuilders.standaloneSetup(gameResource)
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
    public static Game createEntity(EntityManager em) {
        Game game = new Game()
            .pointsA(DEFAULT_POINTS_A)
            .pointsB(DEFAULT_POINTS_B)
            .splitDeckNum(DEFAULT_SPLIT_DECK_NUM)
            .timeLeft(DEFAULT_TIME_LEFT)
            .status(DEFAULT_STATUS);
        // Add required entity
        Fixture fixture;
        if (TestUtil.findAll(em, Fixture.class).isEmpty()) {
            fixture = FixtureResourceIT.createEntity(em);
            em.persist(fixture);
            em.flush();
        } else {
            fixture = TestUtil.findAll(em, Fixture.class).get(0);
        }
        game.setFixture(fixture);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        game.setTeamA(team);
        // Add required entity
        game.setTeamB(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        game.setEventCategory(eventCategory);
        return game;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createUpdatedEntity(EntityManager em) {
        Game game = new Game()
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .splitDeckNum(UPDATED_SPLIT_DECK_NUM)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS);
        // Add required entity
        Fixture fixture;
        if (TestUtil.findAll(em, Fixture.class).isEmpty()) {
            fixture = FixtureResourceIT.createUpdatedEntity(em);
            em.persist(fixture);
            em.flush();
        } else {
            fixture = TestUtil.findAll(em, Fixture.class).get(0);
        }
        game.setFixture(fixture);
        // Add required entity
        Team team;
        if (TestUtil.findAll(em, Team.class).isEmpty()) {
            team = TeamResourceIT.createUpdatedEntity(em);
            em.persist(team);
            em.flush();
        } else {
            team = TestUtil.findAll(em, Team.class).get(0);
        }
        game.setTeamA(team);
        // Add required entity
        game.setTeamB(team);
        // Add required entity
        EventCategory eventCategory;
        if (TestUtil.findAll(em, EventCategory.class).isEmpty()) {
            eventCategory = EventCategoryResourceIT.createUpdatedEntity(em);
            em.persist(eventCategory);
            em.flush();
        } else {
            eventCategory = TestUtil.findAll(em, EventCategory.class).get(0);
        }
        game.setEventCategory(eventCategory);
        return game;
    }

    @BeforeEach
    public void initTest() {
        game = createEntity(em);
    }

    @Test
    @Transactional
    public void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);
        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getPointsA()).isEqualTo(DEFAULT_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(DEFAULT_POINTS_B);
        assertThat(testGame.getSplitDeckNum()).isEqualTo(DEFAULT_SPLIT_DECK_NUM);
        assertThat(testGame.getTimeLeft()).isEqualTo(DEFAULT_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createGameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // Create the Game with an existing ID
        game.setId(1L);
        GameDTO gameDTO = gameMapper.toDto(game);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setStatus(null);

        // Create the Game, which fails.
        GameDTO gameDTO = gameMapper.toDto(game);

        restGameMockMvc.perform(post("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGames() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList
        restGameMockMvc.perform(get("/api/games?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].pointsA").value(hasItem(DEFAULT_POINTS_A)))
            .andExpect(jsonPath("$.[*].pointsB").value(hasItem(DEFAULT_POINTS_B)))
            .andExpect(jsonPath("$.[*].splitDeckNum").value(hasItem(DEFAULT_SPLIT_DECK_NUM)))
            .andExpect(jsonPath("$.[*].timeLeft").value(hasItem(DEFAULT_TIME_LEFT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId().intValue()))
            .andExpect(jsonPath("$.pointsA").value(DEFAULT_POINTS_A))
            .andExpect(jsonPath("$.pointsB").value(DEFAULT_POINTS_B))
            .andExpect(jsonPath("$.splitDeckNum").value(DEFAULT_SPLIT_DECK_NUM))
            .andExpect(jsonPath("$.timeLeft").value(DEFAULT_TIME_LEFT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getAllGamesByPointsAIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA equals to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.equals=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA equals to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.equals=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA not equals to DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.notEquals=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA not equals to UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.notEquals=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsAIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA in DEFAULT_POINTS_A or UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.in=" + DEFAULT_POINTS_A + "," + UPDATED_POINTS_A);

        // Get all the gameList where pointsA equals to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.in=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsAIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is not null
        defaultGameShouldBeFound("pointsA.specified=true");

        // Get all the gameList where pointsA is null
        defaultGameShouldNotBeFound("pointsA.specified=false");
    }

    @Test
    @Transactional
    public void getAllGamesByPointsAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is greater than or equal to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.greaterThanOrEqual=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is greater than or equal to UPDATED_POINTS_A
        defaultGameShouldNotBeFound("pointsA.greaterThanOrEqual=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is less than or equal to DEFAULT_POINTS_A
        defaultGameShouldBeFound("pointsA.lessThanOrEqual=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is less than or equal to SMALLER_POINTS_A
        defaultGameShouldNotBeFound("pointsA.lessThanOrEqual=" + SMALLER_POINTS_A);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsAIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is less than DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.lessThan=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is less than UPDATED_POINTS_A
        defaultGameShouldBeFound("pointsA.lessThan=" + UPDATED_POINTS_A);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsA is greater than DEFAULT_POINTS_A
        defaultGameShouldNotBeFound("pointsA.greaterThan=" + DEFAULT_POINTS_A);

        // Get all the gameList where pointsA is greater than SMALLER_POINTS_A
        defaultGameShouldBeFound("pointsA.greaterThan=" + SMALLER_POINTS_A);
    }


    @Test
    @Transactional
    public void getAllGamesByPointsBIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB equals to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.equals=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB equals to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.equals=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB not equals to DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.notEquals=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB not equals to UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.notEquals=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsBIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB in DEFAULT_POINTS_B or UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.in=" + DEFAULT_POINTS_B + "," + UPDATED_POINTS_B);

        // Get all the gameList where pointsB equals to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.in=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsBIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is not null
        defaultGameShouldBeFound("pointsB.specified=true");

        // Get all the gameList where pointsB is null
        defaultGameShouldNotBeFound("pointsB.specified=false");
    }

    @Test
    @Transactional
    public void getAllGamesByPointsBIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is greater than or equal to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.greaterThanOrEqual=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is greater than or equal to UPDATED_POINTS_B
        defaultGameShouldNotBeFound("pointsB.greaterThanOrEqual=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsBIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is less than or equal to DEFAULT_POINTS_B
        defaultGameShouldBeFound("pointsB.lessThanOrEqual=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is less than or equal to SMALLER_POINTS_B
        defaultGameShouldNotBeFound("pointsB.lessThanOrEqual=" + SMALLER_POINTS_B);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsBIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is less than DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.lessThan=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is less than UPDATED_POINTS_B
        defaultGameShouldBeFound("pointsB.lessThan=" + UPDATED_POINTS_B);
    }

    @Test
    @Transactional
    public void getAllGamesByPointsBIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where pointsB is greater than DEFAULT_POINTS_B
        defaultGameShouldNotBeFound("pointsB.greaterThan=" + DEFAULT_POINTS_B);

        // Get all the gameList where pointsB is greater than SMALLER_POINTS_B
        defaultGameShouldBeFound("pointsB.greaterThan=" + SMALLER_POINTS_B);
    }


    @Test
    @Transactional
    public void getAllGamesBySplitDeckNumIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum equals to DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.equals=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum equals to UPDATED_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.equals=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    public void getAllGamesBySplitDeckNumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum not equals to DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.notEquals=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum not equals to UPDATED_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.notEquals=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    public void getAllGamesBySplitDeckNumIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum in DEFAULT_SPLIT_DECK_NUM or UPDATED_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.in=" + DEFAULT_SPLIT_DECK_NUM + "," + UPDATED_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum equals to UPDATED_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.in=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    public void getAllGamesBySplitDeckNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is not null
        defaultGameShouldBeFound("splitDeckNum.specified=true");

        // Get all the gameList where splitDeckNum is null
        defaultGameShouldNotBeFound("splitDeckNum.specified=false");
    }

    @Test
    @Transactional
    public void getAllGamesBySplitDeckNumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is greater than or equal to DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.greaterThanOrEqual=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum is greater than or equal to UPDATED_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.greaterThanOrEqual=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    public void getAllGamesBySplitDeckNumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is less than or equal to DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.lessThanOrEqual=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum is less than or equal to SMALLER_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.lessThanOrEqual=" + SMALLER_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    public void getAllGamesBySplitDeckNumIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is less than DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.lessThan=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum is less than UPDATED_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.lessThan=" + UPDATED_SPLIT_DECK_NUM);
    }

    @Test
    @Transactional
    public void getAllGamesBySplitDeckNumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where splitDeckNum is greater than DEFAULT_SPLIT_DECK_NUM
        defaultGameShouldNotBeFound("splitDeckNum.greaterThan=" + DEFAULT_SPLIT_DECK_NUM);

        // Get all the gameList where splitDeckNum is greater than SMALLER_SPLIT_DECK_NUM
        defaultGameShouldBeFound("splitDeckNum.greaterThan=" + SMALLER_SPLIT_DECK_NUM);
    }


    @Test
    @Transactional
    public void getAllGamesByTimeLeftIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft equals to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.equals=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft equals to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.equals=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    public void getAllGamesByTimeLeftIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft not equals to DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.notEquals=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft not equals to UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.notEquals=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    public void getAllGamesByTimeLeftIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft in DEFAULT_TIME_LEFT or UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.in=" + DEFAULT_TIME_LEFT + "," + UPDATED_TIME_LEFT);

        // Get all the gameList where timeLeft equals to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.in=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    public void getAllGamesByTimeLeftIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is not null
        defaultGameShouldBeFound("timeLeft.specified=true");

        // Get all the gameList where timeLeft is null
        defaultGameShouldNotBeFound("timeLeft.specified=false");
    }

    @Test
    @Transactional
    public void getAllGamesByTimeLeftIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is greater than or equal to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.greaterThanOrEqual=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is greater than or equal to UPDATED_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.greaterThanOrEqual=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    public void getAllGamesByTimeLeftIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is less than or equal to DEFAULT_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.lessThanOrEqual=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is less than or equal to SMALLER_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.lessThanOrEqual=" + SMALLER_TIME_LEFT);
    }

    @Test
    @Transactional
    public void getAllGamesByTimeLeftIsLessThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is less than DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.lessThan=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is less than UPDATED_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.lessThan=" + UPDATED_TIME_LEFT);
    }

    @Test
    @Transactional
    public void getAllGamesByTimeLeftIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where timeLeft is greater than DEFAULT_TIME_LEFT
        defaultGameShouldNotBeFound("timeLeft.greaterThan=" + DEFAULT_TIME_LEFT);

        // Get all the gameList where timeLeft is greater than SMALLER_TIME_LEFT
        defaultGameShouldBeFound("timeLeft.greaterThan=" + SMALLER_TIME_LEFT);
    }


    @Test
    @Transactional
    public void getAllGamesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status equals to DEFAULT_STATUS
        defaultGameShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the gameList where status equals to UPDATED_STATUS
        defaultGameShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllGamesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status not equals to DEFAULT_STATUS
        defaultGameShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the gameList where status not equals to UPDATED_STATUS
        defaultGameShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllGamesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultGameShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the gameList where status equals to UPDATED_STATUS
        defaultGameShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllGamesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where status is not null
        defaultGameShouldBeFound("status.specified=true");

        // Get all the gameList where status is null
        defaultGameShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllGamesByFixtureIsEqualToSomething() throws Exception {
        // Get already existing entity
        Fixture fixture = game.getFixture();
        gameRepository.saveAndFlush(game);
        Long fixtureId = fixture.getId();

        // Get all the gameList where fixture equals to fixtureId
        defaultGameShouldBeFound("fixtureId.equals=" + fixtureId);

        // Get all the gameList where fixture equals to fixtureId + 1
        defaultGameShouldNotBeFound("fixtureId.equals=" + (fixtureId + 1));
    }


    @Test
    @Transactional
    public void getAllGamesByTeamAIsEqualToSomething() throws Exception {
        // Get already existing entity
        Team teamA = game.getTeamA();
        gameRepository.saveAndFlush(game);
        Long teamAId = teamA.getId();

        // Get all the gameList where teamA equals to teamAId
        defaultGameShouldBeFound("teamAId.equals=" + teamAId);

        // Get all the gameList where teamA equals to teamAId + 1
        defaultGameShouldNotBeFound("teamAId.equals=" + (teamAId + 1));
    }


    @Test
    @Transactional
    public void getAllGamesByTeamBIsEqualToSomething() throws Exception {
        // Get already existing entity
        Team teamB = game.getTeamB();
        gameRepository.saveAndFlush(game);
        Long teamBId = teamB.getId();

        // Get all the gameList where teamB equals to teamBId
        defaultGameShouldBeFound("teamBId.equals=" + teamBId);

        // Get all the gameList where teamB equals to teamBId + 1
        defaultGameShouldNotBeFound("teamBId.equals=" + (teamBId + 1));
    }


    @Test
    @Transactional
    public void getAllGamesByEventCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        EventCategory eventCategory = game.getEventCategory();
        gameRepository.saveAndFlush(game);
        Long eventCategoryId = eventCategory.getId();

        // Get all the gameList where eventCategory equals to eventCategoryId
        defaultGameShouldBeFound("eventCategoryId.equals=" + eventCategoryId);

        // Get all the gameList where eventCategory equals to eventCategoryId + 1
        defaultGameShouldNotBeFound("eventCategoryId.equals=" + (eventCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGameShouldBeFound(String filter) throws Exception {
        restGameMockMvc.perform(get("/api/games?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].pointsA").value(hasItem(DEFAULT_POINTS_A)))
            .andExpect(jsonPath("$.[*].pointsB").value(hasItem(DEFAULT_POINTS_B)))
            .andExpect(jsonPath("$.[*].splitDeckNum").value(hasItem(DEFAULT_SPLIT_DECK_NUM)))
            .andExpect(jsonPath("$.[*].timeLeft").value(hasItem(DEFAULT_TIME_LEFT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restGameMockMvc.perform(get("/api/games/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGameShouldNotBeFound(String filter) throws Exception {
        restGameMockMvc.perform(get("/api/games?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGameMockMvc.perform(get("/api/games/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get("/api/games/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findById(game.getId()).get();
        // Disconnect from session so that the updates on updatedGame are not directly saved in db
        em.detach(updatedGame);
        updatedGame
            .pointsA(UPDATED_POINTS_A)
            .pointsB(UPDATED_POINTS_B)
            .splitDeckNum(UPDATED_SPLIT_DECK_NUM)
            .timeLeft(UPDATED_TIME_LEFT)
            .status(UPDATED_STATUS);
        GameDTO gameDTO = gameMapper.toDto(updatedGame);

        restGameMockMvc.perform(put("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getPointsA()).isEqualTo(UPDATED_POINTS_A);
        assertThat(testGame.getPointsB()).isEqualTo(UPDATED_POINTS_B);
        assertThat(testGame.getSplitDeckNum()).isEqualTo(UPDATED_SPLIT_DECK_NUM);
        assertThat(testGame.getTimeLeft()).isEqualTo(UPDATED_TIME_LEFT);
        assertThat(testGame.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Create the Game
        GameDTO gameDTO = gameMapper.toDto(game);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc.perform(put("/api/games")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Delete the game
        restGameMockMvc.perform(delete("/api/games/{id}", game.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Game.class);
        Game game1 = new Game();
        game1.setId(1L);
        Game game2 = new Game();
        game2.setId(game1.getId());
        assertThat(game1).isEqualTo(game2);
        game2.setId(2L);
        assertThat(game1).isNotEqualTo(game2);
        game1.setId(null);
        assertThat(game1).isNotEqualTo(game2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameDTO.class);
        GameDTO gameDTO1 = new GameDTO();
        gameDTO1.setId(1L);
        GameDTO gameDTO2 = new GameDTO();
        assertThat(gameDTO1).isNotEqualTo(gameDTO2);
        gameDTO2.setId(gameDTO1.getId());
        assertThat(gameDTO1).isEqualTo(gameDTO2);
        gameDTO2.setId(2L);
        assertThat(gameDTO1).isNotEqualTo(gameDTO2);
        gameDTO1.setId(null);
        assertThat(gameDTO1).isNotEqualTo(gameDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(gameMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(gameMapper.fromId(null)).isNull();
    }
}
