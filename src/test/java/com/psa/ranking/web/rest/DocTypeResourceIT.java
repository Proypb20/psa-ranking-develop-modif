package com.psa.ranking.web.rest;

import com.psa.ranking.PsaRankingApp;
import com.psa.ranking.domain.DocType;
import com.psa.ranking.repository.DocTypeRepository;
import com.psa.ranking.service.DocTypeService;
import com.psa.ranking.service.dto.DocTypeDTO;
import com.psa.ranking.service.mapper.DocTypeMapper;
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
import java.util.List;

import static com.psa.ranking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DocTypeResource} REST controller.
 */
@SpringBootTest(classes = PsaRankingApp.class)
public class DocTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DocTypeRepository docTypeRepository;

    @Autowired
    private DocTypeMapper docTypeMapper;

    @Autowired
    private DocTypeService docTypeService;

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

    private MockMvc restDocTypeMockMvc;

    private DocType docType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocTypeResource docTypeResource = new DocTypeResource(docTypeService);
        this.restDocTypeMockMvc = MockMvcBuilders.standaloneSetup(docTypeResource)
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
    public static DocType createEntity(EntityManager em) {
        DocType docType = new DocType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return docType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocType createUpdatedEntity(EntityManager em) {
        DocType docType = new DocType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return docType;
    }

    @BeforeEach
    public void initTest() {
        docType = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocType() throws Exception {
        int databaseSizeBeforeCreate = docTypeRepository.findAll().size();

        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);
        restDocTypeMockMvc.perform(post("/api/doc-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DocType testDocType = docTypeList.get(docTypeList.size() - 1);
        assertThat(testDocType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDocTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = docTypeRepository.findAll().size();

        // Create the DocType with an existing ID
        docType.setId(1L);
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocTypeMockMvc.perform(post("/api/doc-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDocTypes() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        // Get all the docTypeList
        restDocTypeMockMvc.perform(get("/api/doc-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(docType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getDocType() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        // Get the docType
        restDocTypeMockMvc.perform(get("/api/doc-types/{id}", docType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(docType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingDocType() throws Exception {
        // Get the docType
        restDocTypeMockMvc.perform(get("/api/doc-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocType() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();

        // Update the docType
        DocType updatedDocType = docTypeRepository.findById(docType.getId()).get();
        // Disconnect from session so that the updates on updatedDocType are not directly saved in db
        em.detach(updatedDocType);
        updatedDocType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(updatedDocType);

        restDocTypeMockMvc.perform(put("/api/doc-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docTypeDTO)))
            .andExpect(status().isOk());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
        DocType testDocType = docTypeList.get(docTypeList.size() - 1);
        assertThat(testDocType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDocType() throws Exception {
        int databaseSizeBeforeUpdate = docTypeRepository.findAll().size();

        // Create the DocType
        DocTypeDTO docTypeDTO = docTypeMapper.toDto(docType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocTypeMockMvc.perform(put("/api/doc-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(docTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocType in the database
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDocType() throws Exception {
        // Initialize the database
        docTypeRepository.saveAndFlush(docType);

        int databaseSizeBeforeDelete = docTypeRepository.findAll().size();

        // Delete the docType
        restDocTypeMockMvc.perform(delete("/api/doc-types/{id}", docType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocType> docTypeList = docTypeRepository.findAll();
        assertThat(docTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocType.class);
        DocType docType1 = new DocType();
        docType1.setId(1L);
        DocType docType2 = new DocType();
        docType2.setId(docType1.getId());
        assertThat(docType1).isEqualTo(docType2);
        docType2.setId(2L);
        assertThat(docType1).isNotEqualTo(docType2);
        docType1.setId(null);
        assertThat(docType1).isNotEqualTo(docType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocTypeDTO.class);
        DocTypeDTO docTypeDTO1 = new DocTypeDTO();
        docTypeDTO1.setId(1L);
        DocTypeDTO docTypeDTO2 = new DocTypeDTO();
        assertThat(docTypeDTO1).isNotEqualTo(docTypeDTO2);
        docTypeDTO2.setId(docTypeDTO1.getId());
        assertThat(docTypeDTO1).isEqualTo(docTypeDTO2);
        docTypeDTO2.setId(2L);
        assertThat(docTypeDTO1).isNotEqualTo(docTypeDTO2);
        docTypeDTO1.setId(null);
        assertThat(docTypeDTO1).isNotEqualTo(docTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(docTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(docTypeMapper.fromId(null)).isNull();
    }
}
