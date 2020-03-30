package com.ar.pbpoints.web.rest;

import com.ar.pbpoints.PbPointsApp;
import com.ar.pbpoints.domain.Event;
import com.ar.pbpoints.domain.Tournament;
import com.ar.pbpoints.domain.City;
import com.ar.pbpoints.repository.EventRepository;
import com.ar.pbpoints.service.EventService;
import com.ar.pbpoints.service.dto.EventDTO;
import com.ar.pbpoints.service.mapper.EventMapper;
import com.ar.pbpoints.web.rest.errors.ExceptionTranslator;
import com.ar.pbpoints.service.dto.EventCriteria;
import com.ar.pbpoints.service.EventQueryService;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.ar.pbpoints.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ar.pbpoints.domain.enumeration.Status;
/**
 * Integration tests for the {@link EventResource} REST controller.
 */
@SpringBootTest(classes = PbPointsApp.class)
public class EventResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FROM_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_INSCRIPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_INSCRIPTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_INSCRIPTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Status DEFAULT_STATUS = Status.CREATED;
    private static final Status UPDATED_STATUS = Status.IN_PROGRESS;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventQueryService eventQueryService;

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

    private MockMvc restEventMockMvc;

    private Event event;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventService, eventQueryService);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
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
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .name(DEFAULT_NAME)
            .fromDate(DEFAULT_FROM_DATE)
            .endDate(DEFAULT_END_DATE)
            .endInscriptionDate(DEFAULT_END_INSCRIPTION_DATE)
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return event;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event()
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .endDate(UPDATED_END_DATE)
            .endInscriptionDate(UPDATED_END_INSCRIPTION_DATE)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEvent.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEvent.getEndInscriptionDate()).isEqualTo(DEFAULT_END_INSCRIPTION_DATE);
        assertThat(testEvent.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testEvent.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testEvent.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId(1L);
        EventDTO eventDTO = eventMapper.toDto(event);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].endInscriptionDate").value(hasItem(DEFAULT_END_INSCRIPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.endInscriptionDate").value(DEFAULT_END_INSCRIPTION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllEventsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name equals to DEFAULT_NAME
        defaultEventShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventList where name equals to UPDATED_NAME
        defaultEventShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEventsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name not equals to DEFAULT_NAME
        defaultEventShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the eventList where name not equals to UPDATED_NAME
        defaultEventShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEventsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventList where name equals to UPDATED_NAME
        defaultEventShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEventsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name is not null
        defaultEventShouldBeFound("name.specified=true");

        // Get all the eventList where name is null
        defaultEventShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllEventsByNameContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name contains DEFAULT_NAME
        defaultEventShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventList where name contains UPDATED_NAME
        defaultEventShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEventsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where name does not contain DEFAULT_NAME
        defaultEventShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventList where name does not contain UPDATED_NAME
        defaultEventShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllEventsByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate equals to DEFAULT_FROM_DATE
        defaultEventShouldBeFound("fromDate.equals=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate equals to UPDATED_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByFromDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate not equals to DEFAULT_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.notEquals=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate not equals to UPDATED_FROM_DATE
        defaultEventShouldBeFound("fromDate.notEquals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate in DEFAULT_FROM_DATE or UPDATED_FROM_DATE
        defaultEventShouldBeFound("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE);

        // Get all the eventList where fromDate equals to UPDATED_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is not null
        defaultEventShouldBeFound("fromDate.specified=true");

        // Get all the eventList where fromDate is null
        defaultEventShouldNotBeFound("fromDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByFromDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is greater than or equal to DEFAULT_FROM_DATE
        defaultEventShouldBeFound("fromDate.greaterThanOrEqual=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate is greater than or equal to UPDATED_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.greaterThanOrEqual=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByFromDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is less than or equal to DEFAULT_FROM_DATE
        defaultEventShouldBeFound("fromDate.lessThanOrEqual=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate is less than or equal to SMALLER_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.lessThanOrEqual=" + SMALLER_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByFromDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is less than DEFAULT_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.lessThan=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate is less than UPDATED_FROM_DATE
        defaultEventShouldBeFound("fromDate.lessThan=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByFromDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where fromDate is greater than DEFAULT_FROM_DATE
        defaultEventShouldNotBeFound("fromDate.greaterThan=" + DEFAULT_FROM_DATE);

        // Get all the eventList where fromDate is greater than SMALLER_FROM_DATE
        defaultEventShouldBeFound("fromDate.greaterThan=" + SMALLER_FROM_DATE);
    }


    @Test
    @Transactional
    public void getAllEventsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate equals to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate equals to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate not equals to DEFAULT_END_DATE
        defaultEventShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate not equals to UPDATED_END_DATE
        defaultEventShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultEventShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the eventList where endDate equals to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is not null
        defaultEventShouldBeFound("endDate.specified=true");

        // Get all the eventList where endDate is null
        defaultEventShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is greater than or equal to UPDATED_END_DATE
        defaultEventShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is less than or equal to DEFAULT_END_DATE
        defaultEventShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is less than or equal to SMALLER_END_DATE
        defaultEventShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is less than DEFAULT_END_DATE
        defaultEventShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is less than UPDATED_END_DATE
        defaultEventShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endDate is greater than DEFAULT_END_DATE
        defaultEventShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the eventList where endDate is greater than SMALLER_END_DATE
        defaultEventShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }


    @Test
    @Transactional
    public void getAllEventsByEndInscriptionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate equals to DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.equals=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate equals to UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.equals=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndInscriptionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate not equals to DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.notEquals=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate not equals to UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.notEquals=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndInscriptionDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate in DEFAULT_END_INSCRIPTION_DATE or UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.in=" + DEFAULT_END_INSCRIPTION_DATE + "," + UPDATED_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate equals to UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.in=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndInscriptionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is not null
        defaultEventShouldBeFound("endInscriptionDate.specified=true");

        // Get all the eventList where endInscriptionDate is null
        defaultEventShouldNotBeFound("endInscriptionDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByEndInscriptionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is greater than or equal to DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.greaterThanOrEqual=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate is greater than or equal to UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.greaterThanOrEqual=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndInscriptionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is less than or equal to DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.lessThanOrEqual=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate is less than or equal to SMALLER_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.lessThanOrEqual=" + SMALLER_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndInscriptionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is less than DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.lessThan=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate is less than UPDATED_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.lessThan=" + UPDATED_END_INSCRIPTION_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByEndInscriptionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where endInscriptionDate is greater than DEFAULT_END_INSCRIPTION_DATE
        defaultEventShouldNotBeFound("endInscriptionDate.greaterThan=" + DEFAULT_END_INSCRIPTION_DATE);

        // Get all the eventList where endInscriptionDate is greater than SMALLER_END_INSCRIPTION_DATE
        defaultEventShouldBeFound("endInscriptionDate.greaterThan=" + SMALLER_END_INSCRIPTION_DATE);
    }


    @Test
    @Transactional
    public void getAllEventsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where status equals to DEFAULT_STATUS
        defaultEventShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the eventList where status equals to UPDATED_STATUS
        defaultEventShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEventsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where status not equals to DEFAULT_STATUS
        defaultEventShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the eventList where status not equals to UPDATED_STATUS
        defaultEventShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEventsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultEventShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the eventList where status equals to UPDATED_STATUS
        defaultEventShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllEventsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where status is not null
        defaultEventShouldBeFound("status.specified=true");

        // Get all the eventList where status is null
        defaultEventShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createDate equals to DEFAULT_CREATE_DATE
        defaultEventShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the eventList where createDate equals to UPDATED_CREATE_DATE
        defaultEventShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createDate not equals to DEFAULT_CREATE_DATE
        defaultEventShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the eventList where createDate not equals to UPDATED_CREATE_DATE
        defaultEventShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultEventShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the eventList where createDate equals to UPDATED_CREATE_DATE
        defaultEventShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where createDate is not null
        defaultEventShouldBeFound("createDate.specified=true");

        // Get all the eventList where createDate is null
        defaultEventShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedDate equals to DEFAULT_UPDATED_DATE
        defaultEventShouldBeFound("updatedDate.equals=" + DEFAULT_UPDATED_DATE);

        // Get all the eventList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultEventShouldNotBeFound("updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByUpdatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedDate not equals to DEFAULT_UPDATED_DATE
        defaultEventShouldNotBeFound("updatedDate.notEquals=" + DEFAULT_UPDATED_DATE);

        // Get all the eventList where updatedDate not equals to UPDATED_UPDATED_DATE
        defaultEventShouldBeFound("updatedDate.notEquals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedDate in DEFAULT_UPDATED_DATE or UPDATED_UPDATED_DATE
        defaultEventShouldBeFound("updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE);

        // Get all the eventList where updatedDate equals to UPDATED_UPDATED_DATE
        defaultEventShouldNotBeFound("updatedDate.in=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEventsByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList where updatedDate is not null
        defaultEventShouldBeFound("updatedDate.specified=true");

        // Get all the eventList where updatedDate is null
        defaultEventShouldNotBeFound("updatedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllEventsByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        event.setTournament(tournament);
        eventRepository.saveAndFlush(event);
        Long tournamentId = tournament.getId();

        // Get all the eventList where tournament equals to tournamentId
        defaultEventShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the eventList where tournament equals to tournamentId + 1
        defaultEventShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }


    @Test
    @Transactional
    public void getAllEventsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);
        City city = CityResourceIT.createEntity(em);
        em.persist(city);
        em.flush();
        event.setCity(city);
        eventRepository.saveAndFlush(event);
        Long cityId = city.getId();

        // Get all the eventList where city equals to cityId
        defaultEventShouldBeFound("cityId.equals=" + cityId);

        // Get all the eventList where city equals to cityId + 1
        defaultEventShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventShouldBeFound(String filter) throws Exception {
        restEventMockMvc.perform(get("/api/events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].endInscriptionDate").value(hasItem(DEFAULT_END_INSCRIPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));

        // Check, that the count call also returns 1
        restEventMockMvc.perform(get("/api/events/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventShouldNotBeFound(String filter) throws Exception {
        restEventMockMvc.perform(get("/api/events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventMockMvc.perform(get("/api/events/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .name(UPDATED_NAME)
            .fromDate(UPDATED_FROM_DATE)
            .endDate(UPDATED_END_DATE)
            .endInscriptionDate(UPDATED_END_INSCRIPTION_DATE)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        EventDTO eventDTO = eventMapper.toDto(updatedEvent);

        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvent.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEvent.getEndInscriptionDate()).isEqualTo(UPDATED_END_INSCRIPTION_DATE);
        assertThat(testEvent.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testEvent.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testEvent.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Create the Event
        EventDTO eventDTO = eventMapper.toDto(event);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);
        event2.setId(2L);
        assertThat(event1).isNotEqualTo(event2);
        event1.setId(null);
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventDTO.class);
        EventDTO eventDTO1 = new EventDTO();
        eventDTO1.setId(1L);
        EventDTO eventDTO2 = new EventDTO();
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
        eventDTO2.setId(eventDTO1.getId());
        assertThat(eventDTO1).isEqualTo(eventDTO2);
        eventDTO2.setId(2L);
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
        eventDTO1.setId(null);
        assertThat(eventDTO1).isNotEqualTo(eventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(eventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(eventMapper.fromId(null)).isNull();
    }
}
