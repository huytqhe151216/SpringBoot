package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Gateway3App;
import com.mycompany.myapp.domain.HuyGenre;
import com.mycompany.myapp.repository.HuyGenreRepository;
import com.mycompany.myapp.repository.search.HuyGenreSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HuyGenreResource} REST controller.
 */
@SpringBootTest(classes = Gateway3App.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class HuyGenreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private HuyGenreRepository huyGenreRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.HuyGenreSearchRepositoryMockConfiguration
     */
    @Autowired
    private HuyGenreSearchRepository mockHuyGenreSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHuyGenreMockMvc;

    private HuyGenre huyGenre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuyGenre createEntity(EntityManager em) {
        HuyGenre huyGenre = new HuyGenre()
            .name(DEFAULT_NAME);
        return huyGenre;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HuyGenre createUpdatedEntity(EntityManager em) {
        HuyGenre huyGenre = new HuyGenre()
            .name(UPDATED_NAME);
        return huyGenre;
    }

    @BeforeEach
    public void initTest() {
        huyGenre = createEntity(em);
    }

    @Test
    @Transactional
    public void createHuyGenre() throws Exception {
        int databaseSizeBeforeCreate = huyGenreRepository.findAll().size();

        // Create the HuyGenre
        restHuyGenreMockMvc.perform(post("/api/huy-genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(huyGenre)))
            .andExpect(status().isCreated());

        // Validate the HuyGenre in the database
        List<HuyGenre> huyGenreList = huyGenreRepository.findAll();
        assertThat(huyGenreList).hasSize(databaseSizeBeforeCreate + 1);
        HuyGenre testHuyGenre = huyGenreList.get(huyGenreList.size() - 1);
        assertThat(testHuyGenre.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the HuyGenre in Elasticsearch
        verify(mockHuyGenreSearchRepository, times(1)).save(testHuyGenre);
    }

    @Test
    @Transactional
    public void createHuyGenreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = huyGenreRepository.findAll().size();

        // Create the HuyGenre with an existing ID
        huyGenre.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHuyGenreMockMvc.perform(post("/api/huy-genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(huyGenre)))
            .andExpect(status().isBadRequest());

        // Validate the HuyGenre in the database
        List<HuyGenre> huyGenreList = huyGenreRepository.findAll();
        assertThat(huyGenreList).hasSize(databaseSizeBeforeCreate);

        // Validate the HuyGenre in Elasticsearch
        verify(mockHuyGenreSearchRepository, times(0)).save(huyGenre);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = huyGenreRepository.findAll().size();
        // set the field null
        huyGenre.setName(null);

        // Create the HuyGenre, which fails.

        restHuyGenreMockMvc.perform(post("/api/huy-genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(huyGenre)))
            .andExpect(status().isBadRequest());

        List<HuyGenre> huyGenreList = huyGenreRepository.findAll();
        assertThat(huyGenreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHuyGenres() throws Exception {
        // Initialize the database
        huyGenreRepository.saveAndFlush(huyGenre);

        // Get all the huyGenreList
        restHuyGenreMockMvc.perform(get("/api/huy-genres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huyGenre.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getHuyGenre() throws Exception {
        // Initialize the database
        huyGenreRepository.saveAndFlush(huyGenre);

        // Get the huyGenre
        restHuyGenreMockMvc.perform(get("/api/huy-genres/{id}", huyGenre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(huyGenre.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingHuyGenre() throws Exception {
        // Get the huyGenre
        restHuyGenreMockMvc.perform(get("/api/huy-genres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHuyGenre() throws Exception {
        // Initialize the database
        huyGenreRepository.saveAndFlush(huyGenre);

        int databaseSizeBeforeUpdate = huyGenreRepository.findAll().size();

        // Update the huyGenre
        HuyGenre updatedHuyGenre = huyGenreRepository.findById(huyGenre.getId()).get();
        // Disconnect from session so that the updates on updatedHuyGenre are not directly saved in db
        em.detach(updatedHuyGenre);
        updatedHuyGenre
            .name(UPDATED_NAME);

        restHuyGenreMockMvc.perform(put("/api/huy-genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHuyGenre)))
            .andExpect(status().isOk());

        // Validate the HuyGenre in the database
        List<HuyGenre> huyGenreList = huyGenreRepository.findAll();
        assertThat(huyGenreList).hasSize(databaseSizeBeforeUpdate);
        HuyGenre testHuyGenre = huyGenreList.get(huyGenreList.size() - 1);
        assertThat(testHuyGenre.getName()).isEqualTo(UPDATED_NAME);

        // Validate the HuyGenre in Elasticsearch
        verify(mockHuyGenreSearchRepository, times(1)).save(testHuyGenre);
    }

    @Test
    @Transactional
    public void updateNonExistingHuyGenre() throws Exception {
        int databaseSizeBeforeUpdate = huyGenreRepository.findAll().size();

        // Create the HuyGenre

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHuyGenreMockMvc.perform(put("/api/huy-genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(huyGenre)))
            .andExpect(status().isBadRequest());

        // Validate the HuyGenre in the database
        List<HuyGenre> huyGenreList = huyGenreRepository.findAll();
        assertThat(huyGenreList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HuyGenre in Elasticsearch
        verify(mockHuyGenreSearchRepository, times(0)).save(huyGenre);
    }

    @Test
    @Transactional
    public void deleteHuyGenre() throws Exception {
        // Initialize the database
        huyGenreRepository.saveAndFlush(huyGenre);

        int databaseSizeBeforeDelete = huyGenreRepository.findAll().size();

        // Delete the huyGenre
        restHuyGenreMockMvc.perform(delete("/api/huy-genres/{id}", huyGenre.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HuyGenre> huyGenreList = huyGenreRepository.findAll();
        assertThat(huyGenreList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HuyGenre in Elasticsearch
        verify(mockHuyGenreSearchRepository, times(1)).deleteById(huyGenre.getId());
    }

    @Test
    @Transactional
    public void searchHuyGenre() throws Exception {
        // Initialize the database
        huyGenreRepository.saveAndFlush(huyGenre);
        when(mockHuyGenreSearchRepository.search(queryStringQuery("id:" + huyGenre.getId())))
            .thenReturn(Collections.singletonList(huyGenre));
        // Search the huyGenre
        restHuyGenreMockMvc.perform(get("/api/_search/huy-genres?query=id:" + huyGenre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(huyGenre.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
}
