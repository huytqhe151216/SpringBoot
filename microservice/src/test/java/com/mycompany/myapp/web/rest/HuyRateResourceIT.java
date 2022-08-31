package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.HuyRate;
import com.mycompany.myapp.repository.HuyRateRepository;
import com.mycompany.myapp.repository.search.HuyRateSearchRepository;
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
 * Integration tests for the {@link HuyRateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HuyRateResourceIT {

    private static final Integer DEFAULT_STAR = 1;
    private static final Integer UPDATED_STAR = 2;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_CREATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/huy-rates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/huy-rates";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HuyRateRepository huyRateRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.HuyRateSearchRepositoryMockConfiguration
     */
    @Autowired
    private HuyRateSearchRepository mockHuyRateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHuyRateMockMvc;

    private HuyRate huyRate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuyRate createEntity(EntityManager em) {
        HuyRate huyRate = new HuyRate().star(DEFAULT_STAR).content(DEFAULT_CONTENT).dateCreate(DEFAULT_DATE_CREATE);
        return huyRate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuyRate createUpdatedEntity(EntityManager em) {
        HuyRate huyRate = new HuyRate().star(UPDATED_STAR).content(UPDATED_CONTENT).dateCreate(UPDATED_DATE_CREATE);
        return huyRate;
    }

    @BeforeEach
    public void initTest() {
        huyRate = createEntity(em);
    }

    @Test
    @Transactional
    void createHuyRate() throws Exception {
        int databaseSizeBeforeCreate = huyRateRepository.findAll().size();
        // Create the HuyRate
        restHuyRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyRate)))
            .andExpect(status().isCreated());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeCreate + 1);
        HuyRate testHuyRate = huyRateList.get(huyRateList.size() - 1);
        assertThat(testHuyRate.getStar()).isEqualTo(DEFAULT_STAR);
        assertThat(testHuyRate.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testHuyRate.getDateCreate()).isEqualTo(DEFAULT_DATE_CREATE);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(1)).save(testHuyRate);
    }

    @Test
    @Transactional
    void createHuyRateWithExistingId() throws Exception {
        // Create the HuyRate with an existing ID
        huyRate.setId(1L);

        int databaseSizeBeforeCreate = huyRateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHuyRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyRate)))
            .andExpect(status().isBadRequest());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeCreate);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(0)).save(huyRate);
    }

    @Test
    @Transactional
    void checkStarIsRequired() throws Exception {
        int databaseSizeBeforeTest = huyRateRepository.findAll().size();
        // set the field null
        huyRate.setStar(null);

        // Create the HuyRate, which fails.

        restHuyRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyRate)))
            .andExpect(status().isBadRequest());

        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHuyRates() throws Exception {
        // Initialize the database
        huyRateRepository.saveAndFlush(huyRate);

        // Get all the huyRateList
        restHuyRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huyRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].dateCreate").value(hasItem(DEFAULT_DATE_CREATE.toString())));
    }

    @Test
    @Transactional
    void getHuyRate() throws Exception {
        // Initialize the database
        huyRateRepository.saveAndFlush(huyRate);

        // Get the huyRate
        restHuyRateMockMvc
            .perform(get(ENTITY_API_URL_ID, huyRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(huyRate.getId().intValue()))
            .andExpect(jsonPath("$.star").value(DEFAULT_STAR))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.dateCreate").value(DEFAULT_DATE_CREATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHuyRate() throws Exception {
        // Get the huyRate
        restHuyRateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHuyRate() throws Exception {
        // Initialize the database
        huyRateRepository.saveAndFlush(huyRate);

        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();

        // Update the huyRate
        HuyRate updatedHuyRate = huyRateRepository.findById(huyRate.getId()).get();
        // Disconnect from session so that the updates on updatedHuyRate are not directly saved in db
        em.detach(updatedHuyRate);
        updatedHuyRate.star(UPDATED_STAR).content(UPDATED_CONTENT).dateCreate(UPDATED_DATE_CREATE);

        restHuyRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHuyRate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHuyRate))
            )
            .andExpect(status().isOk());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);
        HuyRate testHuyRate = huyRateList.get(huyRateList.size() - 1);
        assertThat(testHuyRate.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testHuyRate.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHuyRate.getDateCreate()).isEqualTo(UPDATED_DATE_CREATE);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository).save(testHuyRate);
    }

    @Test
    @Transactional
    void putNonExistingHuyRate() throws Exception {
        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();
        huyRate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuyRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, huyRate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huyRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(0)).save(huyRate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHuyRate() throws Exception {
        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();
        huyRate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huyRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(0)).save(huyRate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHuyRate() throws Exception {
        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();
        huyRate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyRateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyRate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(0)).save(huyRate);
    }

    @Test
    @Transactional
    void partialUpdateHuyRateWithPatch() throws Exception {
        // Initialize the database
        huyRateRepository.saveAndFlush(huyRate);

        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();

        // Update the huyRate using partial update
        HuyRate partialUpdatedHuyRate = new HuyRate();
        partialUpdatedHuyRate.setId(huyRate.getId());

        partialUpdatedHuyRate.star(UPDATED_STAR);

        restHuyRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuyRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuyRate))
            )
            .andExpect(status().isOk());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);
        HuyRate testHuyRate = huyRateList.get(huyRateList.size() - 1);
        assertThat(testHuyRate.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testHuyRate.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testHuyRate.getDateCreate()).isEqualTo(DEFAULT_DATE_CREATE);
    }

    @Test
    @Transactional
    void fullUpdateHuyRateWithPatch() throws Exception {
        // Initialize the database
        huyRateRepository.saveAndFlush(huyRate);

        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();

        // Update the huyRate using partial update
        HuyRate partialUpdatedHuyRate = new HuyRate();
        partialUpdatedHuyRate.setId(huyRate.getId());

        partialUpdatedHuyRate.star(UPDATED_STAR).content(UPDATED_CONTENT).dateCreate(UPDATED_DATE_CREATE);

        restHuyRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuyRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuyRate))
            )
            .andExpect(status().isOk());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);
        HuyRate testHuyRate = huyRateList.get(huyRateList.size() - 1);
        assertThat(testHuyRate.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testHuyRate.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHuyRate.getDateCreate()).isEqualTo(UPDATED_DATE_CREATE);
    }

    @Test
    @Transactional
    void patchNonExistingHuyRate() throws Exception {
        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();
        huyRate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuyRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, huyRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huyRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(0)).save(huyRate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHuyRate() throws Exception {
        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();
        huyRate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huyRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(0)).save(huyRate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHuyRate() throws Exception {
        int databaseSizeBeforeUpdate = huyRateRepository.findAll().size();
        huyRate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyRateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(huyRate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HuyRate in the database
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(0)).save(huyRate);
    }

    @Test
    @Transactional
    void deleteHuyRate() throws Exception {
        // Initialize the database
        huyRateRepository.saveAndFlush(huyRate);

        int databaseSizeBeforeDelete = huyRateRepository.findAll().size();

        // Delete the huyRate
        restHuyRateMockMvc
            .perform(delete(ENTITY_API_URL_ID, huyRate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HuyRate> huyRateList = huyRateRepository.findAll();
        assertThat(huyRateList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HuyRate in Elasticsearch
        verify(mockHuyRateSearchRepository, times(1)).deleteById(huyRate.getId());
    }

    @Test
    @Transactional
    void searchHuyRate() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        huyRateRepository.saveAndFlush(huyRate);
        when(mockHuyRateSearchRepository.search(queryStringQuery("id:" + huyRate.getId()))).thenReturn(Collections.singletonList(huyRate));

        // Search the huyRate
        restHuyRateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + huyRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huyRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].dateCreate").value(hasItem(DEFAULT_DATE_CREATE.toString())));
    }
}
