package com.ar.pbpoints.web.rest;

import com.ar.pbpoints.PbPointsApp;
import com.ar.pbpoints.domain.Formula;
import com.ar.pbpoints.domain.Tournament;
import com.ar.pbpoints.repository.FormulaRepository;
import com.ar.pbpoints.service.FormulaService;
import com.ar.pbpoints.service.dto.FormulaDTO;
import com.ar.pbpoints.service.mapper.FormulaMapper;
import com.ar.pbpoints.web.rest.errors.ExceptionTranslator;
import com.ar.pbpoints.service.dto.FormulaCriteria;
import com.ar.pbpoints.service.FormulaQueryService;

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
 * Integration tests for the {@link FormulaResource} REST controller.
 */
@SpringBootTest(classes = PbPointsApp.class)
public class FormulaResourceIT {

    private static final String DEFAULT_FORMULA = "AAAAAAAAAA";
    private static final String UPDATED_FORMULA = "BBBBBBBBBB";

    private static final String DEFAULT_VAR_1 = "AAAAAAAAAA";
    private static final String UPDATED_VAR_1 = "BBBBBBBBBB";

    private static final String DEFAULT_VAR_2 = "AAAAAAAAAA";
    private static final String UPDATED_VAR_2 = "BBBBBBBBBB";

    private static final String DEFAULT_VAR_3 = "AAAAAAAAAA";
    private static final String UPDATED_VAR_3 = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EXAMPLE = "AAAAAAAAAA";
    private static final String UPDATED_EXAMPLE = "BBBBBBBBBB";

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private FormulaMapper formulaMapper;

    @Autowired
    private FormulaService formulaService;

    @Autowired
    private FormulaQueryService formulaQueryService;

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

    private MockMvc restFormulaMockMvc;

    private Formula formula;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormulaResource formulaResource = new FormulaResource(formulaService, formulaQueryService);
        this.restFormulaMockMvc = MockMvcBuilders.standaloneSetup(formulaResource)
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
    public static Formula createEntity(EntityManager em) {
        Formula formula = new Formula()
            .formula(DEFAULT_FORMULA)
            .var1(DEFAULT_VAR_1)
            .var2(DEFAULT_VAR_2)
            .var3(DEFAULT_VAR_3)
            .description(DEFAULT_DESCRIPTION)
            .example(DEFAULT_EXAMPLE);
        return formula;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createUpdatedEntity(EntityManager em) {
        Formula formula = new Formula()
            .formula(UPDATED_FORMULA)
            .var1(UPDATED_VAR_1)
            .var2(UPDATED_VAR_2)
            .var3(UPDATED_VAR_3)
            .description(UPDATED_DESCRIPTION)
            .example(UPDATED_EXAMPLE);
        return formula;
    }

    @BeforeEach
    public void initTest() {
        formula = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormula() throws Exception {
        int databaseSizeBeforeCreate = formulaRepository.findAll().size();

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);
        restFormulaMockMvc.perform(post("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isCreated());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeCreate + 1);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getFormula()).isEqualTo(DEFAULT_FORMULA);
        assertThat(testFormula.getVar1()).isEqualTo(DEFAULT_VAR_1);
        assertThat(testFormula.getVar2()).isEqualTo(DEFAULT_VAR_2);
        assertThat(testFormula.getVar3()).isEqualTo(DEFAULT_VAR_3);
        assertThat(testFormula.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormula.getExample()).isEqualTo(DEFAULT_EXAMPLE);
    }

    @Test
    @Transactional
    public void createFormulaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formulaRepository.findAll().size();

        // Create the Formula with an existing ID
        formula.setId(1L);
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormulaMockMvc.perform(post("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFormulaIsRequired() throws Exception {
        int databaseSizeBeforeTest = formulaRepository.findAll().size();
        // set the field null
        formula.setFormula(null);

        // Create the Formula, which fails.
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        restFormulaMockMvc.perform(post("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isBadRequest());

        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVar1IsRequired() throws Exception {
        int databaseSizeBeforeTest = formulaRepository.findAll().size();
        // set the field null
        formula.setVar1(null);

        // Create the Formula, which fails.
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        restFormulaMockMvc.perform(post("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isBadRequest());

        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = formulaRepository.findAll().size();
        // set the field null
        formula.setDescription(null);

        // Create the Formula, which fails.
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        restFormulaMockMvc.perform(post("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isBadRequest());

        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormulas() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList
        restFormulaMockMvc.perform(get("/api/formulas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formula.getId().intValue())))
            .andExpect(jsonPath("$.[*].formula").value(hasItem(DEFAULT_FORMULA)))
            .andExpect(jsonPath("$.[*].var1").value(hasItem(DEFAULT_VAR_1)))
            .andExpect(jsonPath("$.[*].var2").value(hasItem(DEFAULT_VAR_2)))
            .andExpect(jsonPath("$.[*].var3").value(hasItem(DEFAULT_VAR_3)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].example").value(hasItem(DEFAULT_EXAMPLE)));
    }
    
    @Test
    @Transactional
    public void getFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get the formula
        restFormulaMockMvc.perform(get("/api/formulas/{id}", formula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formula.getId().intValue()))
            .andExpect(jsonPath("$.formula").value(DEFAULT_FORMULA))
            .andExpect(jsonPath("$.var1").value(DEFAULT_VAR_1))
            .andExpect(jsonPath("$.var2").value(DEFAULT_VAR_2))
            .andExpect(jsonPath("$.var3").value(DEFAULT_VAR_3))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.example").value(DEFAULT_EXAMPLE));
    }

    @Test
    @Transactional
    public void getAllFormulasByFormulaIsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where formula equals to DEFAULT_FORMULA
        defaultFormulaShouldBeFound("formula.equals=" + DEFAULT_FORMULA);

        // Get all the formulaList where formula equals to UPDATED_FORMULA
        defaultFormulaShouldNotBeFound("formula.equals=" + UPDATED_FORMULA);
    }

    @Test
    @Transactional
    public void getAllFormulasByFormulaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where formula not equals to DEFAULT_FORMULA
        defaultFormulaShouldNotBeFound("formula.notEquals=" + DEFAULT_FORMULA);

        // Get all the formulaList where formula not equals to UPDATED_FORMULA
        defaultFormulaShouldBeFound("formula.notEquals=" + UPDATED_FORMULA);
    }

    @Test
    @Transactional
    public void getAllFormulasByFormulaIsInShouldWork() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where formula in DEFAULT_FORMULA or UPDATED_FORMULA
        defaultFormulaShouldBeFound("formula.in=" + DEFAULT_FORMULA + "," + UPDATED_FORMULA);

        // Get all the formulaList where formula equals to UPDATED_FORMULA
        defaultFormulaShouldNotBeFound("formula.in=" + UPDATED_FORMULA);
    }

    @Test
    @Transactional
    public void getAllFormulasByFormulaIsNullOrNotNull() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where formula is not null
        defaultFormulaShouldBeFound("formula.specified=true");

        // Get all the formulaList where formula is null
        defaultFormulaShouldNotBeFound("formula.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormulasByFormulaContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where formula contains DEFAULT_FORMULA
        defaultFormulaShouldBeFound("formula.contains=" + DEFAULT_FORMULA);

        // Get all the formulaList where formula contains UPDATED_FORMULA
        defaultFormulaShouldNotBeFound("formula.contains=" + UPDATED_FORMULA);
    }

    @Test
    @Transactional
    public void getAllFormulasByFormulaNotContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where formula does not contain DEFAULT_FORMULA
        defaultFormulaShouldNotBeFound("formula.doesNotContain=" + DEFAULT_FORMULA);

        // Get all the formulaList where formula does not contain UPDATED_FORMULA
        defaultFormulaShouldBeFound("formula.doesNotContain=" + UPDATED_FORMULA);
    }


    @Test
    @Transactional
    public void getAllFormulasByVar1IsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var1 equals to DEFAULT_VAR_1
        defaultFormulaShouldBeFound("var1.equals=" + DEFAULT_VAR_1);

        // Get all the formulaList where var1 equals to UPDATED_VAR_1
        defaultFormulaShouldNotBeFound("var1.equals=" + UPDATED_VAR_1);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var1 not equals to DEFAULT_VAR_1
        defaultFormulaShouldNotBeFound("var1.notEquals=" + DEFAULT_VAR_1);

        // Get all the formulaList where var1 not equals to UPDATED_VAR_1
        defaultFormulaShouldBeFound("var1.notEquals=" + UPDATED_VAR_1);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar1IsInShouldWork() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var1 in DEFAULT_VAR_1 or UPDATED_VAR_1
        defaultFormulaShouldBeFound("var1.in=" + DEFAULT_VAR_1 + "," + UPDATED_VAR_1);

        // Get all the formulaList where var1 equals to UPDATED_VAR_1
        defaultFormulaShouldNotBeFound("var1.in=" + UPDATED_VAR_1);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar1IsNullOrNotNull() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var1 is not null
        defaultFormulaShouldBeFound("var1.specified=true");

        // Get all the formulaList where var1 is null
        defaultFormulaShouldNotBeFound("var1.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormulasByVar1ContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var1 contains DEFAULT_VAR_1
        defaultFormulaShouldBeFound("var1.contains=" + DEFAULT_VAR_1);

        // Get all the formulaList where var1 contains UPDATED_VAR_1
        defaultFormulaShouldNotBeFound("var1.contains=" + UPDATED_VAR_1);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar1NotContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var1 does not contain DEFAULT_VAR_1
        defaultFormulaShouldNotBeFound("var1.doesNotContain=" + DEFAULT_VAR_1);

        // Get all the formulaList where var1 does not contain UPDATED_VAR_1
        defaultFormulaShouldBeFound("var1.doesNotContain=" + UPDATED_VAR_1);
    }


    @Test
    @Transactional
    public void getAllFormulasByVar2IsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var2 equals to DEFAULT_VAR_2
        defaultFormulaShouldBeFound("var2.equals=" + DEFAULT_VAR_2);

        // Get all the formulaList where var2 equals to UPDATED_VAR_2
        defaultFormulaShouldNotBeFound("var2.equals=" + UPDATED_VAR_2);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var2 not equals to DEFAULT_VAR_2
        defaultFormulaShouldNotBeFound("var2.notEquals=" + DEFAULT_VAR_2);

        // Get all the formulaList where var2 not equals to UPDATED_VAR_2
        defaultFormulaShouldBeFound("var2.notEquals=" + UPDATED_VAR_2);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar2IsInShouldWork() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var2 in DEFAULT_VAR_2 or UPDATED_VAR_2
        defaultFormulaShouldBeFound("var2.in=" + DEFAULT_VAR_2 + "," + UPDATED_VAR_2);

        // Get all the formulaList where var2 equals to UPDATED_VAR_2
        defaultFormulaShouldNotBeFound("var2.in=" + UPDATED_VAR_2);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar2IsNullOrNotNull() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var2 is not null
        defaultFormulaShouldBeFound("var2.specified=true");

        // Get all the formulaList where var2 is null
        defaultFormulaShouldNotBeFound("var2.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormulasByVar2ContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var2 contains DEFAULT_VAR_2
        defaultFormulaShouldBeFound("var2.contains=" + DEFAULT_VAR_2);

        // Get all the formulaList where var2 contains UPDATED_VAR_2
        defaultFormulaShouldNotBeFound("var2.contains=" + UPDATED_VAR_2);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar2NotContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var2 does not contain DEFAULT_VAR_2
        defaultFormulaShouldNotBeFound("var2.doesNotContain=" + DEFAULT_VAR_2);

        // Get all the formulaList where var2 does not contain UPDATED_VAR_2
        defaultFormulaShouldBeFound("var2.doesNotContain=" + UPDATED_VAR_2);
    }


    @Test
    @Transactional
    public void getAllFormulasByVar3IsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var3 equals to DEFAULT_VAR_3
        defaultFormulaShouldBeFound("var3.equals=" + DEFAULT_VAR_3);

        // Get all the formulaList where var3 equals to UPDATED_VAR_3
        defaultFormulaShouldNotBeFound("var3.equals=" + UPDATED_VAR_3);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var3 not equals to DEFAULT_VAR_3
        defaultFormulaShouldNotBeFound("var3.notEquals=" + DEFAULT_VAR_3);

        // Get all the formulaList where var3 not equals to UPDATED_VAR_3
        defaultFormulaShouldBeFound("var3.notEquals=" + UPDATED_VAR_3);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar3IsInShouldWork() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var3 in DEFAULT_VAR_3 or UPDATED_VAR_3
        defaultFormulaShouldBeFound("var3.in=" + DEFAULT_VAR_3 + "," + UPDATED_VAR_3);

        // Get all the formulaList where var3 equals to UPDATED_VAR_3
        defaultFormulaShouldNotBeFound("var3.in=" + UPDATED_VAR_3);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar3IsNullOrNotNull() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var3 is not null
        defaultFormulaShouldBeFound("var3.specified=true");

        // Get all the formulaList where var3 is null
        defaultFormulaShouldNotBeFound("var3.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormulasByVar3ContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var3 contains DEFAULT_VAR_3
        defaultFormulaShouldBeFound("var3.contains=" + DEFAULT_VAR_3);

        // Get all the formulaList where var3 contains UPDATED_VAR_3
        defaultFormulaShouldNotBeFound("var3.contains=" + UPDATED_VAR_3);
    }

    @Test
    @Transactional
    public void getAllFormulasByVar3NotContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where var3 does not contain DEFAULT_VAR_3
        defaultFormulaShouldNotBeFound("var3.doesNotContain=" + DEFAULT_VAR_3);

        // Get all the formulaList where var3 does not contain UPDATED_VAR_3
        defaultFormulaShouldBeFound("var3.doesNotContain=" + UPDATED_VAR_3);
    }


    @Test
    @Transactional
    public void getAllFormulasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description equals to DEFAULT_DESCRIPTION
        defaultFormulaShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the formulaList where description equals to UPDATED_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormulasByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description not equals to DEFAULT_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the formulaList where description not equals to UPDATED_DESCRIPTION
        defaultFormulaShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormulasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFormulaShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the formulaList where description equals to UPDATED_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormulasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description is not null
        defaultFormulaShouldBeFound("description.specified=true");

        // Get all the formulaList where description is null
        defaultFormulaShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormulasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description contains DEFAULT_DESCRIPTION
        defaultFormulaShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the formulaList where description contains UPDATED_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFormulasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where description does not contain DEFAULT_DESCRIPTION
        defaultFormulaShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the formulaList where description does not contain UPDATED_DESCRIPTION
        defaultFormulaShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllFormulasByExampleIsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where example equals to DEFAULT_EXAMPLE
        defaultFormulaShouldBeFound("example.equals=" + DEFAULT_EXAMPLE);

        // Get all the formulaList where example equals to UPDATED_EXAMPLE
        defaultFormulaShouldNotBeFound("example.equals=" + UPDATED_EXAMPLE);
    }

    @Test
    @Transactional
    public void getAllFormulasByExampleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where example not equals to DEFAULT_EXAMPLE
        defaultFormulaShouldNotBeFound("example.notEquals=" + DEFAULT_EXAMPLE);

        // Get all the formulaList where example not equals to UPDATED_EXAMPLE
        defaultFormulaShouldBeFound("example.notEquals=" + UPDATED_EXAMPLE);
    }

    @Test
    @Transactional
    public void getAllFormulasByExampleIsInShouldWork() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where example in DEFAULT_EXAMPLE or UPDATED_EXAMPLE
        defaultFormulaShouldBeFound("example.in=" + DEFAULT_EXAMPLE + "," + UPDATED_EXAMPLE);

        // Get all the formulaList where example equals to UPDATED_EXAMPLE
        defaultFormulaShouldNotBeFound("example.in=" + UPDATED_EXAMPLE);
    }

    @Test
    @Transactional
    public void getAllFormulasByExampleIsNullOrNotNull() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where example is not null
        defaultFormulaShouldBeFound("example.specified=true");

        // Get all the formulaList where example is null
        defaultFormulaShouldNotBeFound("example.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormulasByExampleContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where example contains DEFAULT_EXAMPLE
        defaultFormulaShouldBeFound("example.contains=" + DEFAULT_EXAMPLE);

        // Get all the formulaList where example contains UPDATED_EXAMPLE
        defaultFormulaShouldNotBeFound("example.contains=" + UPDATED_EXAMPLE);
    }

    @Test
    @Transactional
    public void getAllFormulasByExampleNotContainsSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList where example does not contain DEFAULT_EXAMPLE
        defaultFormulaShouldNotBeFound("example.doesNotContain=" + DEFAULT_EXAMPLE);

        // Get all the formulaList where example does not contain UPDATED_EXAMPLE
        defaultFormulaShouldBeFound("example.doesNotContain=" + UPDATED_EXAMPLE);
    }


    @Test
    @Transactional
    public void getAllFormulasByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        formula.setTournament(tournament);
        formulaRepository.saveAndFlush(formula);
        Long tournamentId = tournament.getId();

        // Get all the formulaList where tournament equals to tournamentId
        defaultFormulaShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the formulaList where tournament equals to tournamentId + 1
        defaultFormulaShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormulaShouldBeFound(String filter) throws Exception {
        restFormulaMockMvc.perform(get("/api/formulas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formula.getId().intValue())))
            .andExpect(jsonPath("$.[*].formula").value(hasItem(DEFAULT_FORMULA)))
            .andExpect(jsonPath("$.[*].var1").value(hasItem(DEFAULT_VAR_1)))
            .andExpect(jsonPath("$.[*].var2").value(hasItem(DEFAULT_VAR_2)))
            .andExpect(jsonPath("$.[*].var3").value(hasItem(DEFAULT_VAR_3)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].example").value(hasItem(DEFAULT_EXAMPLE)));

        // Check, that the count call also returns 1
        restFormulaMockMvc.perform(get("/api/formulas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormulaShouldNotBeFound(String filter) throws Exception {
        restFormulaMockMvc.perform(get("/api/formulas?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormulaMockMvc.perform(get("/api/formulas/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFormula() throws Exception {
        // Get the formula
        restFormulaMockMvc.perform(get("/api/formulas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Update the formula
        Formula updatedFormula = formulaRepository.findById(formula.getId()).get();
        // Disconnect from session so that the updates on updatedFormula are not directly saved in db
        em.detach(updatedFormula);
        updatedFormula
            .formula(UPDATED_FORMULA)
            .var1(UPDATED_VAR_1)
            .var2(UPDATED_VAR_2)
            .var3(UPDATED_VAR_3)
            .description(UPDATED_DESCRIPTION)
            .example(UPDATED_EXAMPLE);
        FormulaDTO formulaDTO = formulaMapper.toDto(updatedFormula);

        restFormulaMockMvc.perform(put("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isOk());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getFormula()).isEqualTo(UPDATED_FORMULA);
        assertThat(testFormula.getVar1()).isEqualTo(UPDATED_VAR_1);
        assertThat(testFormula.getVar2()).isEqualTo(UPDATED_VAR_2);
        assertThat(testFormula.getVar3()).isEqualTo(UPDATED_VAR_3);
        assertThat(testFormula.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormula.getExample()).isEqualTo(UPDATED_EXAMPLE);
    }

    @Test
    @Transactional
    public void updateNonExistingFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaMockMvc.perform(put("/api/formulas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formulaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeDelete = formulaRepository.findAll().size();

        // Delete the formula
        restFormulaMockMvc.perform(delete("/api/formulas/{id}", formula.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Formula.class);
        Formula formula1 = new Formula();
        formula1.setId(1L);
        Formula formula2 = new Formula();
        formula2.setId(formula1.getId());
        assertThat(formula1).isEqualTo(formula2);
        formula2.setId(2L);
        assertThat(formula1).isNotEqualTo(formula2);
        formula1.setId(null);
        assertThat(formula1).isNotEqualTo(formula2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormulaDTO.class);
        FormulaDTO formulaDTO1 = new FormulaDTO();
        formulaDTO1.setId(1L);
        FormulaDTO formulaDTO2 = new FormulaDTO();
        assertThat(formulaDTO1).isNotEqualTo(formulaDTO2);
        formulaDTO2.setId(formulaDTO1.getId());
        assertThat(formulaDTO1).isEqualTo(formulaDTO2);
        formulaDTO2.setId(2L);
        assertThat(formulaDTO1).isNotEqualTo(formulaDTO2);
        formulaDTO1.setId(null);
        assertThat(formulaDTO1).isNotEqualTo(formulaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(formulaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(formulaMapper.fromId(null)).isNull();
    }
}
