package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.HuyMovie;
import com.mycompany.myapp.repository.HuyMovieRepository;
import com.mycompany.myapp.repository.search.HuyMovieSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.HuyMovie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HuyMovieResource {

    private final Logger log = LoggerFactory.getLogger(HuyMovieResource.class);

    private static final String ENTITY_NAME = "microserviceHuyMovie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HuyMovieRepository huyMovieRepository;

    private final HuyMovieSearchRepository huyMovieSearchRepository;

    public HuyMovieResource(HuyMovieRepository huyMovieRepository, HuyMovieSearchRepository huyMovieSearchRepository) {
        this.huyMovieRepository = huyMovieRepository;
        this.huyMovieSearchRepository = huyMovieSearchRepository;
    }

    /**
     * {@code POST  /huy-movies} : Create a new huyMovie.
     *
     * @param huyMovie the huyMovie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new huyMovie, or with status {@code 400 (Bad Request)} if the huyMovie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/huy-movies")
    public ResponseEntity<HuyMovie> createHuyMovie(@Valid @RequestBody HuyMovie huyMovie) throws URISyntaxException {
        log.debug("REST request to save HuyMovie : {}", huyMovie);
        if (huyMovie.getId() != null) {
            throw new BadRequestAlertException("A new huyMovie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HuyMovie result = huyMovieRepository.save(huyMovie);
        huyMovieSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/huy-movies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /huy-movies/:id} : Updates an existing huyMovie.
     *
     * @param id the id of the huyMovie to save.
     * @param huyMovie the huyMovie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyMovie,
     * or with status {@code 400 (Bad Request)} if the huyMovie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the huyMovie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/huy-movies/{id}")
    public ResponseEntity<HuyMovie> updateHuyMovie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HuyMovie huyMovie
    ) throws URISyntaxException {
        log.debug("REST request to update HuyMovie : {}, {}", id, huyMovie);
        if (huyMovie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huyMovie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huyMovieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HuyMovie result = huyMovieRepository.save(huyMovie);
        huyMovieSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyMovie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /huy-movies/:id} : Partial updates given fields of an existing huyMovie, field will ignore if it is null
     *
     * @param id the id of the huyMovie to save.
     * @param huyMovie the huyMovie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyMovie,
     * or with status {@code 400 (Bad Request)} if the huyMovie is not valid,
     * or with status {@code 404 (Not Found)} if the huyMovie is not found,
     * or with status {@code 500 (Internal Server Error)} if the huyMovie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/huy-movies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HuyMovie> partialUpdateHuyMovie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HuyMovie huyMovie
    ) throws URISyntaxException {
        log.debug("REST request to partial update HuyMovie partially : {}, {}", id, huyMovie);
        if (huyMovie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huyMovie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huyMovieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HuyMovie> result = huyMovieRepository
            .findById(huyMovie.getId())
            .map(
                existingHuyMovie -> {
                    if (huyMovie.getName() != null) {
                        existingHuyMovie.setName(huyMovie.getName());
                    }
                    if (huyMovie.getDirector() != null) {
                        existingHuyMovie.setDirector(huyMovie.getDirector());
                    }
                    if (huyMovie.getCountry() != null) {
                        existingHuyMovie.setCountry(huyMovie.getCountry());
                    }
                    if (huyMovie.getWriter() != null) {
                        existingHuyMovie.setWriter(huyMovie.getWriter());
                    }
                    if (huyMovie.getDuration() != null) {
                        existingHuyMovie.setDuration(huyMovie.getDuration());
                    }
                    if (huyMovie.getPublishDate() != null) {
                        existingHuyMovie.setPublishDate(huyMovie.getPublishDate());
                    }
                    if (huyMovie.getContentSummary() != null) {
                        existingHuyMovie.setContentSummary(huyMovie.getContentSummary());
                    }

                    return existingHuyMovie;
                }
            )
            .map(huyMovieRepository::save)
            .map(
                savedHuyMovie -> {
                    huyMovieSearchRepository.save(savedHuyMovie);

                    return savedHuyMovie;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyMovie.getId().toString())
        );
    }

    /**
     * {@code GET  /huy-movies} : get all the huyMovies.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of huyMovies in body.
     */
    @GetMapping("/huy-movies")
    public List<HuyMovie> getAllHuyMovies(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all HuyMovies");
        return huyMovieRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /huy-movies/:id} : get the "id" huyMovie.
     *
     * @param id the id of the huyMovie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the huyMovie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/huy-movies/{id}")
    public ResponseEntity<HuyMovie> getHuyMovie(@PathVariable Long id) {
        log.debug("REST request to get HuyMovie : {}", id);
        Optional<HuyMovie> huyMovie = huyMovieRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(huyMovie);
    }

    /**
     * {@code DELETE  /huy-movies/:id} : delete the "id" huyMovie.
     *
     * @param id the id of the huyMovie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/huy-movies/{id}")
    public ResponseEntity<Void> deleteHuyMovie(@PathVariable Long id) {
        log.debug("REST request to delete HuyMovie : {}", id);
        huyMovieRepository.deleteById(id);
        huyMovieSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/huy-movies?query=:query} : search for the huyMovie corresponding
     * to the query.
     *
     * @param query the query of the huyMovie search.
     * @return the result of the search.
     */
    @GetMapping("/_search/huy-movies")
    public List<HuyMovie> searchHuyMovies(@RequestParam String query) {
        log.debug("REST request to search HuyMovies for query {}", query);
        return StreamSupport
            .stream(huyMovieSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
