package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.HuyGenre;
import com.mycompany.myapp.repository.HuyGenreRepository;
import com.mycompany.myapp.repository.search.HuyGenreSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.HuyGenre}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HuyGenreResource {

    private final Logger log = LoggerFactory.getLogger(HuyGenreResource.class);

    private static final String ENTITY_NAME = "huyGenre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HuyGenreRepository huyGenreRepository;

    private final HuyGenreSearchRepository huyGenreSearchRepository;

    public HuyGenreResource(HuyGenreRepository huyGenreRepository, HuyGenreSearchRepository huyGenreSearchRepository) {
        this.huyGenreRepository = huyGenreRepository;
        this.huyGenreSearchRepository = huyGenreSearchRepository;
    }

    /**
     * {@code POST  /huy-genres} : Create a new huyGenre.
     *
     * @param huyGenre the huyGenre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new huyGenre, or with status {@code 400 (Bad Request)} if the huyGenre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/huy-genres")
    public ResponseEntity<HuyGenre> createHuyGenre(@Valid @RequestBody HuyGenre huyGenre) throws URISyntaxException {
        log.debug("REST request to save HuyGenre : {}", huyGenre);
        if (huyGenre.getId() != null) {
            throw new BadRequestAlertException("A new huyGenre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HuyGenre result = huyGenreRepository.save(huyGenre);
        huyGenreSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/huy-genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /huy-genres} : Updates an existing huyGenre.
     *
     * @param huyGenre the huyGenre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyGenre,
     * or with status {@code 400 (Bad Request)} if the huyGenre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the huyGenre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/huy-genres")
    public ResponseEntity<HuyGenre> updateHuyGenre(@Valid @RequestBody HuyGenre huyGenre) throws URISyntaxException {
        log.debug("REST request to update HuyGenre : {}", huyGenre);
        if (huyGenre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HuyGenre result = huyGenreRepository.save(huyGenre);
        huyGenreSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyGenre.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /huy-genres} : get all the huyGenres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of huyGenres in body.
     */
    @GetMapping("/huy-genres")
    public List<HuyGenre> getAllHuyGenres() {
        log.debug("REST request to get all HuyGenres");
        return huyGenreRepository.findAll();
    }

    /**
     * {@code GET  /huy-genres/:id} : get the "id" huyGenre.
     *
     * @param id the id of the huyGenre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the huyGenre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/huy-genres/{id}")
    public ResponseEntity<HuyGenre> getHuyGenre(@PathVariable Long id) {
        log.debug("REST request to get HuyGenre : {}", id);
        Optional<HuyGenre> huyGenre = huyGenreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(huyGenre);
    }

    /**
     * {@code DELETE  /huy-genres/:id} : delete the "id" huyGenre.
     *
     * @param id the id of the huyGenre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/huy-genres/{id}")
    public ResponseEntity<Void> deleteHuyGenre(@PathVariable Long id) {
        log.debug("REST request to delete HuyGenre : {}", id);
        huyGenreRepository.deleteById(id);
        huyGenreSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/huy-genres?query=:query} : search for the huyGenre corresponding
     * to the query.
     *
     * @param query the query of the huyGenre search.
     * @return the result of the search.
     */
    @GetMapping("/_search/huy-genres")
    public List<HuyGenre> searchHuyGenres(@RequestParam String query) {
        log.debug("REST request to search HuyGenres for query {}", query);
        return StreamSupport
            .stream(huyGenreSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
