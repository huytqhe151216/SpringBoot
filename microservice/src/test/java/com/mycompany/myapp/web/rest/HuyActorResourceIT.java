package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.HuyActor;
import com.mycompany.myapp.repository.HuyActorRepository;
import com.mycompany.myapp.repository.search.HuyActorSearchRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HuyActorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HuyActorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DOB = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOB = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/huy-actors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/huy-actors";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HuyActorRepository huyActorRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.HuyActorSearchRepositoryMockConfiguration
     */
    @Autowired
    private HuyActorSearchRepository mockHuyActorSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHuyActorMockMvc;

    private HuyActor huyActor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuyActor createEntity(EntityManager em) {
        HuyActor huyActor = new HuyActor().name(DEFAULT_NAME).dob(DEFAULT_DOB).nationality(DEFAULT_NATIONALITY);
        return huyActor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuyActor createUpdatedEntity(EntityManager em) {
        HuyActor huyActor = new HuyActor().name(UPDATED_NAME).dob(UPDATED_DOB).nationality(UPDATED_NATIONALITY);
        return huyActor;
    }

    @BeforeEach
    public void initTest() {
        huyActor = createEntity(em);
    }

    @Test
    @Transactional
    void createHuyActor() throws Exception {
        int databaseSizeBeforeCreate = huyActorRepository.findAll().size();
        // Create the HuyActor
        restHuyActorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyActor)))
            .andExpect(status().isCreated());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeCreate + 1);
        HuyActor testHuyActor = huyActorList.get(huyActorList.size() - 1);
        assertThat(testHuyActor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHuyActor.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testHuyActor.getNationality()).isEqualTo(DEFAULT_NATIONALITY);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(1)).save(testHuyActor);
    }

    @Test
    @Transactional
    void createHuyActorWithExistingId() throws Exception {
        // Create the HuyActor with an existing ID
        huyActor.setId(1L);

        int databaseSizeBeforeCreate = huyActorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHuyActorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyActor)))
            .andExpect(status().isBadRequest());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeCreate);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(0)).save(huyActor);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = huyActorRepository.findAll().size();
        // set the field null
        huyActor.setName(null);

        // Create the HuyActor, which fails.

        restHuyActorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyActor)))
            .andExpect(status().isBadRequest());

        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHuyActors() throws Exception {
        // Initialize the database
        huyActorRepository.saveAndFlush(huyActor);

        // Get all the huyActorList
        restHuyActorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huyActor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)));
    }

    @Test
    @Transactional
    void getHuyActor() throws Exception {
        // Initialize the database
        huyActorRepository.saveAndFlush(huyActor);

        // Get the huyActor
        restHuyActorMockMvc
            .perform(get(ENTITY_API_URL_ID, huyActor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(huyActor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY));
    }

    @Test
    @Transactional
    void getNonExistingHuyActor() throws Exception {
        // Get the huyActor
        restHuyActorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHuyActor() throws Exception {
        // Initialize the database
        huyActorRepository.saveAndFlush(huyActor);

        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();

        // Update the huyActor
        HuyActor updatedHuyActor = huyActorRepository.findById(huyActor.getId()).get();
        // Disconnect from session so that the updates on updatedHuyActor are not directly saved in db
        em.detach(updatedHuyActor);
        updatedHuyActor.name(UPDATED_NAME).dob(UPDATED_DOB).nationality(UPDATED_NATIONALITY);

        restHuyActorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHuyActor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHuyActor))
            )
            .andExpect(status().isOk());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);
        HuyActor testHuyActor = huyActorList.get(huyActorList.size() - 1);
        assertThat(testHuyActor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHuyActor.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testHuyActor.getNationality()).isEqualTo(UPDATED_NATIONALITY);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository).save(testHuyActor);
    }

    @Test
    @Transactional
    void putNonExistingHuyActor() throws Exception {
        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();
        huyActor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuyActorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, huyActor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huyActor))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(0)).save(huyActor);
    }

    @Test
    @Transactional
    void putWithIdMismatchHuyActor() throws Exception {
        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();
        huyActor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyActorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huyActor))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(0)).save(huyActor);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHuyActor() throws Exception {
        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();
        huyActor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyActorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyActor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(0)).save(huyActor);
    }

    @Test
    @Transactional
    void partialUpdateHuyActorWithPatch() throws Exception {
        // Initialize the database
        huyActorRepository.saveAndFlush(huyActor);

        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();

        // Update the huyActor using partial update
        HuyActor partialUpdatedHuyActor = new HuyActor();
        partialUpdatedHuyActor.setId(huyActor.getId());

        partialUpdatedHuyActor.name(UPDATED_NAME).dob(UPDATED_DOB).nationality(UPDATED_NATIONALITY);

        restHuyActorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuyActor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuyActor))
            )
            .andExpect(status().isOk());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);
        HuyActor testHuyActor = huyActorList.get(huyActorList.size() - 1);
        assertThat(testHuyActor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHuyActor.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testHuyActor.getNationality()).isEqualTo(UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void fullUpdateHuyActorWithPatch() throws Exception {
        // Initialize the database
        huyActorRepository.saveAndFlush(huyActor);

        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();

        // Update the huyActor using partial update
        HuyActor partialUpdatedHuyActor = new HuyActor();
        partialUpdatedHuyActor.setId(huyActor.getId());

        partialUpdatedHuyActor.name(UPDATED_NAME).dob(UPDATED_DOB).nationality(UPDATED_NATIONALITY);

        restHuyActorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuyActor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuyActor))
            )
            .andExpect(status().isOk());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);
        HuyActor testHuyActor = huyActorList.get(huyActorList.size() - 1);
        assertThat(testHuyActor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHuyActor.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testHuyActor.getNationality()).isEqualTo(UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void patchNonExistingHuyActor() throws Exception {
        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();
        huyActor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuyActorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, huyActor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huyActor))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(0)).save(huyActor);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHuyActor() throws Exception {
        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();
        huyActor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyActorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huyActor))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(0)).save(huyActor);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHuyActor() throws Exception {
        int databaseSizeBeforeUpdate = huyActorRepository.findAll().size();
        huyActor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyActorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(huyActor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HuyActor in the database
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(0)).save(huyActor);
    }

    @Test
    @Transactional
    void deleteHuyActor() throws Exception {
        // Initialize the database
        huyActorRepository.saveAndFlush(huyActor);

        int databaseSizeBeforeDelete = huyActorRepository.findAll().size();

        // Delete the huyActor
        restHuyActorMockMvc
            .perform(delete(ENTITY_API_URL_ID, huyActor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HuyActor> huyActorList = huyActorRepository.findAll();
        assertThat(huyActorList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HuyActor in Elasticsearch
        verify(mockHuyActorSearchRepository, times(1)).deleteById(huyActor.getId());
    }

    @Test
    @Transactional
    void searchHuyActor() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        huyActorRepository.saveAndFlush(huyActor);
        when(mockHuyActorSearchRepository.search(queryStringQuery("id:" + huyActor.getId())))
            .thenReturn(Collections.singletonList(huyActor));

        // Search the huyActor
        restHuyActorMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + huyActor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huyActor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)));
    }
}
