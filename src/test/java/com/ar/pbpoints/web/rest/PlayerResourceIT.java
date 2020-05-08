package com.ar.pbpoints.web.rest;

import static com.ar.pbpoints.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import com.ar.pbpoints.repository.UserRepository;
import com.ar.pbpoints.service.RosterService;
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

import com.ar.pbpoints.PbPointsApp;
import com.ar.pbpoints.domain.Player;
import com.ar.pbpoints.domain.Roster;
import com.ar.pbpoints.domain.User;
import com.ar.pbpoints.domain.enumeration.ProfileUser;
import com.ar.pbpoints.repository.PlayerRepository;
import com.ar.pbpoints.service.PlayerQueryService;
import com.ar.pbpoints.service.PlayerService;
import com.ar.pbpoints.service.dto.PlayerDTO;
import com.ar.pbpoints.service.mapper.PlayerMapper;
import com.ar.pbpoints.web.rest.errors.ExceptionTranslator;
/**
 * Integration tests for the {@link PlayerResource} REST controller.
 */
@SpringBootTest(classes = PbPointsApp.class)
public class PlayerResourceIT {

    private static final ProfileUser DEFAULT_PROFILE = ProfileUser.PLAYER;
    private static final ProfileUser UPDATED_PROFILE = ProfileUser.STAFF;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerQueryService playerQueryService;

    @Autowired
    private RosterService rosterService;

    @Autowired
    private UserRepository userRepository;

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

    private MockMvc restPlayerMockMvc;

    private Player player;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerResource playerResource = new PlayerResource(playerService, playerQueryService, rosterService, userRepository);
        this.restPlayerMockMvc = MockMvcBuilders.standaloneSetup(playerResource)
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
    public static Player createEntity(EntityManager em) {
        Player player = new Player()
            .profile(DEFAULT_PROFILE);
        // Add required entity
        Roster roster;
        if (TestUtil.findAll(em, Roster.class).isEmpty()) {
            roster = RosterResourceIT.createEntity(em);
            em.persist(roster);
            em.flush();
        } else {
            roster = TestUtil.findAll(em, Roster.class).get(0);
        }
        player.setRoster(roster);
        return player;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createUpdatedEntity(EntityManager em) {
        Player player = new Player()
            .profile(UPDATED_PROFILE);
        // Add required entity
        Roster roster;
        if (TestUtil.findAll(em, Roster.class).isEmpty()) {
            roster = RosterResourceIT.createUpdatedEntity(em);
            em.persist(roster);
            em.flush();
        } else {
            roster = TestUtil.findAll(em, Roster.class).get(0);
        }
        player.setRoster(roster);
        return player;
    }

    @BeforeEach
    public void initTest() {
        player = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player
        PlayerDTO playerDTO = playerMapper.toDto(player);
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getProfile()).isEqualTo(DEFAULT_PROFILE);
    }

    @Test
    @Transactional
    public void createPlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player with an existing ID
        player.setId(1L);
        PlayerDTO playerDTO = playerMapper.toDto(player);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].profile").value(hasItem(DEFAULT_PROFILE.toString())));
    }

    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(player.getId().intValue()))
            .andExpect(jsonPath("$.profile").value(DEFAULT_PROFILE.toString()));
    }

    @Test
    @Transactional
    public void getAllPlayersByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where profile equals to DEFAULT_PROFILE
        defaultPlayerShouldBeFound("profile.equals=" + DEFAULT_PROFILE);

        // Get all the playerList where profile equals to UPDATED_PROFILE
        defaultPlayerShouldNotBeFound("profile.equals=" + UPDATED_PROFILE);
    }

    @Test
    @Transactional
    public void getAllPlayersByProfileIsNotEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where profile not equals to DEFAULT_PROFILE
        defaultPlayerShouldNotBeFound("profile.notEquals=" + DEFAULT_PROFILE);

        // Get all the playerList where profile not equals to UPDATED_PROFILE
        defaultPlayerShouldBeFound("profile.notEquals=" + UPDATED_PROFILE);
    }

    @Test
    @Transactional
    public void getAllPlayersByProfileIsInShouldWork() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where profile in DEFAULT_PROFILE or UPDATED_PROFILE
        defaultPlayerShouldBeFound("profile.in=" + DEFAULT_PROFILE + "," + UPDATED_PROFILE);

        // Get all the playerList where profile equals to UPDATED_PROFILE
        defaultPlayerShouldNotBeFound("profile.in=" + UPDATED_PROFILE);
    }

    @Test
    @Transactional
    public void getAllPlayersByProfileIsNullOrNotNull() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList where profile is not null
        defaultPlayerShouldBeFound("profile.specified=true");

        // Get all the playerList where profile is null
        defaultPlayerShouldNotBeFound("profile.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlayersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        player.setUser(user);
        playerRepository.saveAndFlush(player);
        Long userId = user.getId();

        // Get all the playerList where user equals to userId
        defaultPlayerShouldBeFound("userId.equals=" + userId);

        // Get all the playerList where user equals to userId + 1
        defaultPlayerShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllPlayersByRosterIsEqualToSomething() throws Exception {
        // Get already existing entity
        Roster roster = player.getRoster();
        playerRepository.saveAndFlush(player);
        Long rosterId = roster.getId();

        // Get all the playerList where roster equals to rosterId
        defaultPlayerShouldBeFound("rosterId.equals=" + rosterId);

        // Get all the playerList where roster equals to rosterId + 1
        defaultPlayerShouldNotBeFound("rosterId.equals=" + (rosterId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlayerShouldBeFound(String filter) throws Exception {
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].profile").value(hasItem(DEFAULT_PROFILE.toString())));

        // Check, that the count call also returns 1
        restPlayerMockMvc.perform(get("/api/players/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlayerShouldNotBeFound(String filter) throws Exception {
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlayerMockMvc.perform(get("/api/players/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        Player updatedPlayer = playerRepository.findById(player.getId()).get();
        // Disconnect from session so that the updates on updatedPlayer are not directly saved in db
        em.detach(updatedPlayer);
        updatedPlayer
            .profile(UPDATED_PROFILE);
        PlayerDTO playerDTO = playerMapper.toDto(updatedPlayer);

        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getProfile()).isEqualTo(UPDATED_PROFILE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayer() throws Exception {
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Create the Player
        PlayerDTO playerDTO = playerMapper.toDto(player);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        int databaseSizeBeforeDelete = playerRepository.findAll().size();

        // Delete the player
        restPlayerMockMvc.perform(delete("/api/players/{id}", player.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Player.class);
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(player1.getId());
        assertThat(player1).isEqualTo(player2);
        player2.setId(2L);
        assertThat(player1).isNotEqualTo(player2);
        player1.setId(null);
        assertThat(player1).isNotEqualTo(player2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerDTO.class);
        PlayerDTO playerDTO1 = new PlayerDTO();
        playerDTO1.setId(1L);
        PlayerDTO playerDTO2 = new PlayerDTO();
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
        playerDTO2.setId(playerDTO1.getId());
        assertThat(playerDTO1).isEqualTo(playerDTO2);
        playerDTO2.setId(2L);
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
        playerDTO1.setId(null);
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(playerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(playerMapper.fromId(null)).isNull();
    }
}
