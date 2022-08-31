package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Gateway3App;
import com.mycompany.myapp.domain.HuyMovie;
import com.mycompany.myapp.repository.HuyMovieRepository;
import com.mycompany.myapp.repository.search.HuyMovieSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HuyMovieResource} REST controller.
 */
@SpringBootTest(classes = Gateway3App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HuyMovieResourceIT {

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
    public void createHuyMovie() throws Exception {
        int databaseSizeBeforeCreate = huyMovieRepository.findAll().size();

        // Create the HuyMovie
        restHuyMovieMockMvc.perform(post("/api/huy-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(huyMovie)))
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
    public void createHuyMovieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = huyMovieRepository.findAll().size();

        // Create the HuyMovie with an existing ID
        huyMovie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHuyMovieMockMvc.perform(post("/api/huy-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(huyMovie)))
            .andExpect(status().isBadRequest());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeCreate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = huyMovieRepository.findAll().size();
        // set the field null
        huyMovie.setName(null);

        // Create the HuyMovie, which fails.

        restHuyMovieMockMvc.perform(post("/api/huy-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(huyMovie)))
            .andExpect(status().isBadRequest());

        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHuyMovies() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        // Get all the huyMovieList
        restHuyMovieMockMvc.perform(get("/api/huy-movies?sort=id,desc"))
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
    
    @SuppressWarnings({"unchecked"})
    public void getAllHuyMoviesWithEagerRelationshipsIsEnabled() throws Exception {
        HuyMovieResource huyMovieResource = new HuyMovieResource(huyMovieRepositoryMock, mockHuyMovieSearchRepository);
        when(huyMovieRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHuyMovieMockMvc.perform(get("/api/huy-movies?eagerload=true"))
            .andExpect(status().isOk());

        verify(huyMovieRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllHuyMoviesWithEagerRelationshipsIsNotEnabled() throws Exception {
        HuyMovieResource huyMovieResource = new HuyMovieResource(huyMovieRepositoryMock, mockHuyMovieSearchRepository);
        when(huyMovieRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHuyMovieMockMvc.perform(get("/api/huy-movies?eagerload=true"))
            .andExpect(status().isOk());

        verify(huyMovieRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getHuyMovie() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        // Get the huyMovie
        restHuyMovieMockMvc.perform(get("/api/huy-movies/{id}", huyMovie.getId()))
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
    public void getNonExistingHuyMovie() throws Exception {
        // Get the huyMovie
        restHuyMovieMockMvc.perform(get("/api/huy-movies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHuyMovie() throws Exception {
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

        restHuyMovieMockMvc.perform(put("/api/huy-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHuyMovie)))
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
        verify(mockHuyMovieSearchRepository, times(1)).save(testHuyMovie);
    }

    @Test
    @Transactional
    public void updateNonExistingHuyMovie() throws Exception {
        int databaseSizeBeforeUpdate = huyMovieRepository.findAll().size();

        // Create the HuyMovie

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuyMovieMockMvc.perform(put("/api/huy-movies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(huyMovie)))
            .andExpect(status().isBadRequest());

        // Validate the HuyMovie in the database
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(0)).save(huyMovie);
    }

    @Test
    @Transactional
    public void deleteHuyMovie() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);

        int databaseSizeBeforeDelete = huyMovieRepository.findAll().size();

        // Delete the huyMovie
        restHuyMovieMockMvc.perform(delete("/api/huy-movies/{id}", huyMovie.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HuyMovie> huyMovieList = huyMovieRepository.findAll();
        assertThat(huyMovieList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HuyMovie in Elasticsearch
        verify(mockHuyMovieSearchRepository, times(1)).deleteById(huyMovie.getId());
    }

    @Test
    @Transactional
    public void searchHuyMovie() throws Exception {
        // Initialize the database
        huyMovieRepository.saveAndFlush(huyMovie);
        when(mockHuyMovieSearchRepository.search(queryStringQuery("id:" + huyMovie.getId())))
            .thenReturn(Collections.singletonList(huyMovie));
        // Search the huyMovie
        restHuyMovieMockMvc.perform(get("/api/_search/huy-movies?query=id:" + huyMovie.getId()))
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
