package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.EventCategory;
import com.psa.ranking.domain.Event;
import com.psa.ranking.domain.Category;
import com.psa.ranking.domain.Format;
import com.psa.ranking.repository.EventCategoryRepository;
import com.psa.ranking.service.EventCategoryService;
import com.psa.ranking.service.dto.EventCategoryDTO;
import com.psa.ranking.service.mapper.EventCategoryMapper;
import com.psa.ranking.web.rest.errors.ExceptionTranslator;
import com.psa.ranking.service.dto.EventCategoryCriteria;
import com.psa.ranking.service.EventCategoryQueryService;

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
 * Integration tests for the {@link EventCategoryResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class EventCategoryResourceIT {

    private static final Boolean DEFAULT_SPLIT_DECK = false;
    private static final Boolean UPDATED_SPLIT_DECK = true;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private EventCategoryMapper eventCategoryMapper;

    @Autowired
    private EventCategoryService eventCategoryService;

    @Autowired
    private EventCategoryQueryService eventCategoryQueryService;

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

    private MockMvc restEventCategoryMockMvc;

    private EventCategory eventCategory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventCategoryResource eventCategoryResource = new EventCategoryResource(eventCategoryService, eventCategoryQueryService);
        this.restEventCategoryMockMvc = MockMvcBuilders.standaloneSetup(eventCategoryResource)
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
    public static EventCategory createEntity(EntityManager em) {
        EventCategory eventCategory = new EventCategory()
            .splitDeck(DEFAULT_SPLIT_DECK);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        eventCategory.setEvent(event);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        eventCategory.setCategory(category);
        // Add required entity
        Format format;
        if (TestUtil.findAll(em, Format.class).isEmpty()) {
            format = FormatResourceIT.createEntity(em);
            em.persist(format);
            em.flush();
        } else {
            format = TestUtil.findAll(em, Format.class).get(0);
        }
        eventCategory.setFormat(format);
        return eventCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCategory createUpdatedEntity(EntityManager em) {
        EventCategory eventCategory = new EventCategory()
            .splitDeck(UPDATED_SPLIT_DECK);
        // Add required entity
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            event = EventResourceIT.createUpdatedEntity(em);
            em.persist(event);
            em.flush();
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        eventCategory.setEvent(event);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        eventCategory.setCategory(category);
        // Add required entity
        Format format;
        if (TestUtil.findAll(em, Format.class).isEmpty()) {
            format = FormatResourceIT.createUpdatedEntity(em);
            em.persist(format);
            em.flush();
        } else {
            format = TestUtil.findAll(em, Format.class).get(0);
        }
        eventCategory.setFormat(format);
        return eventCategory;
    }

    @BeforeEach
    public void initTest() {
        eventCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventCategory() throws Exception {
        int databaseSizeBeforeCreate = eventCategoryRepository.findAll().size();

        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);
        restEventCategoryMockMvc.perform(post("/api/event-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO)))
            .andExpect(status().isCreated());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        EventCategory testEventCategory = eventCategoryList.get(eventCategoryList.size() - 1);
        assertThat(testEventCategory.isSplitDeck()).isEqualTo(DEFAULT_SPLIT_DECK);
    }

    @Test
    @Transactional
    public void createEventCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventCategoryRepository.findAll().size();

        // Create the EventCategory with an existing ID
        eventCategory.setId(1L);
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventCategoryMockMvc.perform(post("/api/event-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEventCategories() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList
        restEventCategoryMockMvc.perform(get("/api/event-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].splitDeck").value(hasItem(DEFAULT_SPLIT_DECK.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getEventCategory() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get the eventCategory
        restEventCategoryMockMvc.perform(get("/api/event-categories/{id}", eventCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventCategory.getId().intValue()))
            .andExpect(jsonPath("$.splitDeck").value(DEFAULT_SPLIT_DECK.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllEventCategoriesBySplitDeckIsEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList where splitDeck equals to DEFAULT_SPLIT_DECK
        defaultEventCategoryShouldBeFound("splitDeck.equals=" + DEFAULT_SPLIT_DECK);

        // Get all the eventCategoryList where splitDeck equals to UPDATED_SPLIT_DECK
        defaultEventCategoryShouldNotBeFound("splitDeck.equals=" + UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    public void getAllEventCategoriesBySplitDeckIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList where splitDeck not equals to DEFAULT_SPLIT_DECK
        defaultEventCategoryShouldNotBeFound("splitDeck.notEquals=" + DEFAULT_SPLIT_DECK);

        // Get all the eventCategoryList where splitDeck not equals to UPDATED_SPLIT_DECK
        defaultEventCategoryShouldBeFound("splitDeck.notEquals=" + UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    public void getAllEventCategoriesBySplitDeckIsInShouldWork() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList where splitDeck in DEFAULT_SPLIT_DECK or UPDATED_SPLIT_DECK
        defaultEventCategoryShouldBeFound("splitDeck.in=" + DEFAULT_SPLIT_DECK + "," + UPDATED_SPLIT_DECK);

        // Get all the eventCategoryList where splitDeck equals to UPDATED_SPLIT_DECK
        defaultEventCategoryShouldNotBeFound("splitDeck.in=" + UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    public void getAllEventCategoriesBySplitDeckIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        // Get all the eventCategoryList where splitDeck is not null
        defaultEventCategoryShouldBeFound("splitDeck.specified=true");

        // Get all the eventCategoryList where splitDeck is null
        defaultEventCategoryShouldNotBeFound("splitDeck.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventCategoriesByEventIsEqualToSomething() throws Exception {
        // Get already existing entity
        Event event = eventCategory.getEvent();
        eventCategoryRepository.saveAndFlush(eventCategory);
        Long eventId = event.getId();

        // Get all the eventCategoryList where event equals to eventId
        defaultEventCategoryShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventCategoryList where event equals to eventId + 1
        defaultEventCategoryShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }


    @Test
    @Transactional
    public void getAllEventCategoriesByCategoryIsEqualToSomething() throws Exception {
        // Get already existing entity
        Category category = eventCategory.getCategory();
        eventCategoryRepository.saveAndFlush(eventCategory);
        Long categoryId = category.getId();

        // Get all the eventCategoryList where category equals to categoryId
        defaultEventCategoryShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the eventCategoryList where category equals to categoryId + 1
        defaultEventCategoryShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllEventCategoriesByFormatIsEqualToSomething() throws Exception {
        // Get already existing entity
        Format format = eventCategory.getFormat();
        eventCategoryRepository.saveAndFlush(eventCategory);
        Long formatId = format.getId();

        // Get all the eventCategoryList where format equals to formatId
        defaultEventCategoryShouldBeFound("formatId.equals=" + formatId);

        // Get all the eventCategoryList where format equals to formatId + 1
        defaultEventCategoryShouldNotBeFound("formatId.equals=" + (formatId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventCategoryShouldBeFound(String filter) throws Exception {
        restEventCategoryMockMvc.perform(get("/api/event-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].splitDeck").value(hasItem(DEFAULT_SPLIT_DECK.booleanValue())));

        // Check, that the count call also returns 1
        restEventCategoryMockMvc.perform(get("/api/event-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventCategoryShouldNotBeFound(String filter) throws Exception {
        restEventCategoryMockMvc.perform(get("/api/event-categories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventCategoryMockMvc.perform(get("/api/event-categories/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEventCategory() throws Exception {
        // Get the eventCategory
        restEventCategoryMockMvc.perform(get("/api/event-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventCategory() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();

        // Update the eventCategory
        EventCategory updatedEventCategory = eventCategoryRepository.findById(eventCategory.getId()).get();
        // Disconnect from session so that the updates on updatedEventCategory are not directly saved in db
        em.detach(updatedEventCategory);
        updatedEventCategory
            .splitDeck(UPDATED_SPLIT_DECK);
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(updatedEventCategory);

        restEventCategoryMockMvc.perform(put("/api/event-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO)))
            .andExpect(status().isOk());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
        EventCategory testEventCategory = eventCategoryList.get(eventCategoryList.size() - 1);
        assertThat(testEventCategory.isSplitDeck()).isEqualTo(UPDATED_SPLIT_DECK);
    }

    @Test
    @Transactional
    public void updateNonExistingEventCategory() throws Exception {
        int databaseSizeBeforeUpdate = eventCategoryRepository.findAll().size();

        // Create the EventCategory
        EventCategoryDTO eventCategoryDTO = eventCategoryMapper.toDto(eventCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventCategoryMockMvc.perform(put("/api/event-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventCategory in the database
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventCategory() throws Exception {
        // Initialize the database
        eventCategoryRepository.saveAndFlush(eventCategory);

        int databaseSizeBeforeDelete = eventCategoryRepository.findAll().size();

        // Delete the eventCategory
        restEventCategoryMockMvc.perform(delete("/api/event-categories/{id}", eventCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventCategory> eventCategoryList = eventCategoryRepository.findAll();
        assertThat(eventCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventCategory.class);
        EventCategory eventCategory1 = new EventCategory();
        eventCategory1.setId(1L);
        EventCategory eventCategory2 = new EventCategory();
        eventCategory2.setId(eventCategory1.getId());
        assertThat(eventCategory1).isEqualTo(eventCategory2);
        eventCategory2.setId(2L);
        assertThat(eventCategory1).isNotEqualTo(eventCategory2);
        eventCategory1.setId(null);
        assertThat(eventCategory1).isNotEqualTo(eventCategory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventCategoryDTO.class);
        EventCategoryDTO eventCategoryDTO1 = new EventCategoryDTO();
        eventCategoryDTO1.setId(1L);
        EventCategoryDTO eventCategoryDTO2 = new EventCategoryDTO();
        assertThat(eventCategoryDTO1).isNotEqualTo(eventCategoryDTO2);
        eventCategoryDTO2.setId(eventCategoryDTO1.getId());
        assertThat(eventCategoryDTO1).isEqualTo(eventCategoryDTO2);
        eventCategoryDTO2.setId(2L);
        assertThat(eventCategoryDTO1).isNotEqualTo(eventCategoryDTO2);
        eventCategoryDTO1.setId(null);
        assertThat(eventCategoryDTO1).isNotEqualTo(eventCategoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(eventCategoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(eventCategoryMapper.fromId(null)).isNull();
    }
}
