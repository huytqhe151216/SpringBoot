package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.HuyMovie;
import com.mycompany.myapp.repository.HuyMovieRepository;
import com.mycompany.myapp.repository.search.HuyMovieSearchRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HuyMovieResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HuyMovieResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTOR = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_WRITER = "AAAAAAAAAA";
    private static final String UPDATED_WRITER = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final Instant DEFAULT_PUBLISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CONTENT_SUMMARY = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_SUMMARY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/huy-movies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/huy-movies";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HuyMovieRepository huyMovieRepository;

    @Mock
    private HuyMovieRepository huyMovieRepositoryMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.HuyMovieSearchRepositoryMockConfiguration
     */
    @Autowired
    private HuyMovieSearchRepository mockHuyMovieSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHuyMovieMockMvc;

    private HuyMovie huyMovie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuyMovie createEntity(EntityManager em) {
        HuyMovie huyMovie = new HuyMovie()
            .name(DEFAULT_NAME)
            .director(DEFAULT_DIRECTOR)
            .country(DEFAULT_COUNTRY)
            .writer(DEFAULT_WRITER)
            .duration(DEFAULT_DURATION)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .contentSummary(DEFAULT_CONTENT_SUMMARY);
        return huyMovie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuyMovie createUpdatedEntity(EntityManager em) {
        HuyMovie huyMovie = new HuyMovie()
            .name(UPDATED_NAME)
            .director(UPDATED_DIRECTOR)
            .country(UPDATED_COUNTRY)
            .writer(UPDATED_WRITER)
            .duration(UPDATED_DURATION)
            .publishDate(UPDATED_PUBLISH_DATE)
            .contentSummary(UPDATED_CONTENT_SUMMARY);
        return huyMovie;
    }

    @BeforeEach
    public void initTest() {
        huyMovie = createEntity(em);
    }

    @Test
    @Transactional
    void createHuyMovie() throws Exception {
        int databaseSizeBeforeCreate = huyMovieRepository.findAll().size();
        // Create the HuyMovie
        restHuyMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyMovie)))
            .andExpect(status().isCreated());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeCreate + 1);
        HuyMovie testHuyMovie = huyMovieList.get(huyMovieList.size() - 1);
        assertThat(testHuyMovie.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHuyMovie.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testHuyMovie.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testHuyMovie.getWriter()).isEqualTo(DEFAULT_WRITER);
        assertThat(testHuyMovie.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testHuyMovie.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testHuyMovie.getContentSummary()).isEqualTo(DEFAULT_CONTENT_SUMMARY);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(1)).save(testHuyMovie);
    }

    @Test
    @Transactional
    void createHuyMovieWithExistingId() throws Exception {
        // Create the HuyMovie with an existing ID
        huyMovie.setId(1L);

        int databaseSizeBeforeCreate = huyMovieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHuyMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyMovie)))
            .andExpect(status().isBadRequest());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeCreate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = huyMovieRepository.findAll().size();
        // set the field null
        huyMovie.setName(null);

        // Create the HuyMovie, which fails.

        restHuyMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyMovie)))
            .andExpect(status().isBadRequest());

        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHuyMovies() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        // Get all the huyMovieList
        restHuyMovieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huyMovie.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].writer").value(hasItem(DEFAULT_WRITER)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].contentSummary").value(hasItem(DEFAULT_CONTENT_SUMMARY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHuyMoviesWithEagerRelationshipsIsEnabled() throws Exception {
        when(huyMovieRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHuyMovieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(huyMovieRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHuyMoviesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(huyMovieRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHuyMovieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(huyMovieRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getHuyMovie() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        // Get the huyMovie
        restHuyMovieMockMvc
            .perform(get(ENTITY_API_URL_ID, huyMovie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(huyMovie.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.director").value(DEFAULT_DIRECTOR))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.writer").value(DEFAULT_WRITER))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()))
            .andExpect(jsonPath("$.contentSummary").value(DEFAULT_CONTENT_SUMMARY));
    }

    @Test
    @Transactional
    void getNonExistingHuyMovie() throws Exception {
        // Get the huyMovie
        restHuyMovieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHuyMovie() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();

        // Update the huyMovie
        HuyMovie updatedHuyMovie = huyMovieRepository.findById(huyMovie.getId()).get();
        // Disconnect from session so that the updates on updatedHuyMovie are not directly saved in db
        em.detach(updatedHuyMovie);
        updatedHuyMovie
            .name(UPDATED_NAME)
            .director(UPDATED_DIRECTOR)
            .country(UPDATED_COUNTRY)
            .writer(UPDATED_WRITER)
            .duration(UPDATED_DURATION)
            .publishDate(UPDATED_PUBLISH_DATE)
            .contentSummary(UPDATED_CONTENT_SUMMARY);

        restHuyMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHuyMovie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHuyMovie))
            )
            .andExpect(status().isOk());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);
        HuyMovie testHuyMovie = huyMovieList.get(huyMovieList.size() - 1);
        assertThat(testHuyMovie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHuyMovie.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testHuyMovie.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testHuyMovie.getWriter()).isEqualTo(UPDATED_WRITER);
        assertThat(testHuyMovie.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testHuyMovie.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testHuyMovie.getContentSummary()).isEqualTo(UPDATED_CONTENT_SUMMARY);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository).save(testHuyMovie);
    }

    @Test
    @Transactional
    void putNonExistingHuyMovie() throws Exception {
        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();
        huyMovie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuyMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, huyMovie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huyMovie))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }

    @Test
    @Transactional
    void putWithIdMismatchHuyMovie() throws Exception {
        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();
        huyMovie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(huyMovie))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHuyMovie() throws Exception {
        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();
        huyMovie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyMovieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(huyMovie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }

    @Test
    @Transactional
    void partialUpdateHuyMovieWithPatch() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();

        // Update the huyMovie using partial update
        HuyMovie partialUpdatedHuyMovie = new HuyMovie();
        partialUpdatedHuyMovie.setId(huyMovie.getId());

        partialUpdatedHuyMovie.country(UPDATED_COUNTRY).duration(UPDATED_DURATION).contentSummary(UPDATED_CONTENT_SUMMARY);

        restHuyMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuyMovie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuyMovie))
            )
            .andExpect(status().isOk());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);
        HuyMovie testHuyMovie = huyMovieList.get(huyMovieList.size() - 1);
        assertThat(testHuyMovie.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHuyMovie.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testHuyMovie.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testHuyMovie.getWriter()).isEqualTo(DEFAULT_WRITER);
        assertThat(testHuyMovie.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testHuyMovie.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testHuyMovie.getContentSummary()).isEqualTo(UPDATED_CONTENT_SUMMARY);
    }

    @Test
    @Transactional
    void fullUpdateHuyMovieWithPatch() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();

        // Update the huyMovie using partial update
        HuyMovie partialUpdatedHuyMovie = new HuyMovie();
        partialUpdatedHuyMovie.setId(huyMovie.getId());

        partialUpdatedHuyMovie
            .name(UPDATED_NAME)
            .director(UPDATED_DIRECTOR)
            .country(UPDATED_COUNTRY)
            .writer(UPDATED_WRITER)
            .duration(UPDATED_DURATION)
            .publishDate(UPDATED_PUBLISH_DATE)
            .contentSummary(UPDATED_CONTENT_SUMMARY);

        restHuyMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuyMovie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuyMovie))
            )
            .andExpect(status().isOk());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);
        HuyMovie testHuyMovie = huyMovieList.get(huyMovieList.size() - 1);
        assertThat(testHuyMovie.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHuyMovie.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testHuyMovie.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testHuyMovie.getWriter()).isEqualTo(UPDATED_WRITER);
        assertThat(testHuyMovie.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testHuyMovie.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testHuyMovie.getContentSummary()).isEqualTo(UPDATED_CONTENT_SUMMARY);
    }

    @Test
    @Transactional
    void patchNonExistingHuyMovie() throws Exception {
        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();
        huyMovie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuyMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, huyMovie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huyMovie))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHuyMovie() throws Exception {
        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();
        huyMovie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(huyMovie))
            )
            .andExpect(status().isBadRequest());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHuyMovie() throws Exception {
        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();
        huyMovie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHuyMovieMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(huyMovie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }

    @Test
    @Transactional
    void deleteHuyMovie() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        int databaseSizeBeforeDelete = huyMovieRepository.findAll().size();

        // Delete the huyMovie
        restHuyMovieMockMvc
            .perform(delete(ENTITY_API_URL_ID, huyMovie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(1)).deleteById(huyMovie.getId());
    }

    @Test
    @Transactional
    void searchHuyMovie() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);
        when(mockHuyMovieSearchRepository.search(queryStringQuery("id:" + huyMovie.getId())))
            .thenReturn(Collections.singletonList(huyMovie));

        // Search the huyMovie
        restHuyMovieMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + huyMovie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huyMovie.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].writer").value(hasItem(DEFAULT_WRITER)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].contentSummary").value(hasItem(DEFAULT_CONTENT_SUMMARY)));
    }
}
