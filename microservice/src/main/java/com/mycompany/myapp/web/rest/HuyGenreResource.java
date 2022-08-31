package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.HuyGenre;
import com.mycompany.myapp.repository.HuyGenreRepository;
import com.mycompany.myapp.repository.search.HuyGenreSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.HuyGenre}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HuyGenreResource {

    private final Logger log = LoggerFactory.getLogger(HuyGenreResource.class);

    private static final String ENTITY_NAME = "microserviceHuyGenre";

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
        return ResponseEntity
            .created(new URI("/api/huy-genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /huy-genres/:id} : Updates an existing huyGenre.
     *
     * @param id the id of the huyGenre to save.
     * @param huyGenre the huyGenre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyGenre,
     * or with status {@code 400 (Bad Request)} if the huyGenre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the huyGenre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/huy-genres/{id}")
    public ResponseEntity<HuyGenre> updateHuyGenre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HuyGenre huyGenre
    ) throws URISyntaxException {
        log.debug("REST request to update HuyGenre : {}, {}", id, huyGenre);
        if (huyGenre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huyGenre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huyGenreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HuyGenre result = huyGenreRepository.save(huyGenre);
        huyGenreSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyGenre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /huy-genres/:id} : Partial updates given fields of an existing huyGenre, field will ignore if it is null
     *
     * @param id the id of the huyGenre to save.
     * @param huyGenre the huyGenre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyGenre,
     * or with status {@code 400 (Bad Request)} if the huyGenre is not valid,
     * or with status {@code 404 (Not Found)} if the huyGenre is not found,
     * or with status {@code 500 (Internal Server Error)} if the huyGenre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/huy-genres/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HuyGenre> partialUpdateHuyGenre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HuyGenre huyGenre
    ) throws URISyntaxException {
        log.debug("REST request to partial update HuyGenre partially : {}, {}", id, huyGenre);
        if (huyGenre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huyGenre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huyGenreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HuyGenre> result = huyGenreRepository
            .findById(huyGenre.getId())
            .map(
                existingHuyGenre -> {
                    if (huyGenre.getName() != null) {
                        existingHuyGenre.setName(huyGenre.getName());
                    }

                    return existingHuyGenre;
                }
            )
            .map(huyGenreRepository::save)
            .map(
                savedHuyGenre -> {
                    huyGenreSearchRepository.save(savedHuyGenre);

                    return savedHuyGenre;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyGenre.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
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
