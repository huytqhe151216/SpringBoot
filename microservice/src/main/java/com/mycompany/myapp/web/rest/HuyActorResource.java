package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.HuyActor;
import com.mycompany.myapp.repository.HuyActorRepository;
import com.mycompany.myapp.repository.search.HuyActorSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.HuyActor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HuyActorResource {

    private final Logger log = LoggerFactory.getLogger(HuyActorResource.class);

    private static final String ENTITY_NAME = "microserviceHuyActor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HuyActorRepository huyActorRepository;

    private final HuyActorSearchRepository huyActorSearchRepository;

    public HuyActorResource(HuyActorRepository huyActorRepository, HuyActorSearchRepository huyActorSearchRepository) {
        this.huyActorRepository = huyActorRepository;
        this.huyActorSearchRepository = huyActorSearchRepository;
    }

    /**
     * {@code POST  /huy-actors} : Create a new huyActor.
     *
     * @param huyActor the huyActor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new huyActor, or with status {@code 400 (Bad Request)} if the huyActor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/huy-actors")
    public ResponseEntity<HuyActor> createHuyActor(@Valid @RequestBody HuyActor huyActor) throws URISyntaxException {
        log.debug("REST request to save HuyActor : {}", huyActor);
        if (huyActor.getId() != null) {
            throw new BadRequestAlertException("A new huyActor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HuyActor result = huyActorRepository.save(huyActor);
        huyActorSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/huy-actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /huy-actors/:id} : Updates an existing huyActor.
     *
     * @param id the id of the huyActor to save.
     * @param huyActor the huyActor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyActor,
     * or with status {@code 400 (Bad Request)} if the huyActor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the huyActor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/huy-actors/{id}")
    public ResponseEntity<HuyActor> updateHuyActor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HuyActor huyActor
    ) throws URISyntaxException {
        log.debug("REST request to update HuyActor : {}, {}", id, huyActor);
        if (huyActor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huyActor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huyActorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HuyActor result = huyActorRepository.save(huyActor);
        huyActorSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyActor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /huy-actors/:id} : Partial updates given fields of an existing huyActor, field will ignore if it is null
     *
     * @param id the id of the huyActor to save.
     * @param huyActor the huyActor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyActor,
     * or with status {@code 400 (Bad Request)} if the huyActor is not valid,
     * or with status {@code 404 (Not Found)} if the huyActor is not found,
     * or with status {@code 500 (Internal Server Error)} if the huyActor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/huy-actors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HuyActor> partialUpdateHuyActor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HuyActor huyActor
    ) throws URISyntaxException {
        log.debug("REST request to partial update HuyActor partially : {}, {}", id, huyActor);
        if (huyActor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, huyActor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!huyActorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HuyActor> result = huyActorRepository
            .findById(huyActor.getId())
            .map(
                existingHuyActor -> {
                    if (huyActor.getName() != null) {
                        existingHuyActor.setName(huyActor.getName());
                    }
                    if (huyActor.getDob() != null) {
                        existingHuyActor.setDob(huyActor.getDob());
                    }
                    if (huyActor.getNationality() != null) {
                        existingHuyActor.setNationality(huyActor.getNationality());
                    }

                    return existingHuyActor;
                }
            )
            .map(huyActorRepository::save)
            .map(
                savedHuyActor -> {
                    huyActorSearchRepository.save(savedHuyActor);

                    return savedHuyActor;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyActor.getId().toString())
        );
    }

    /**
     * {@code GET  /huy-actors} : get all the huyActors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of huyActors in body.
     */
    @GetMapping("/huy-actors")
    public List<HuyActor> getAllHuyActors() {
        log.debug("REST request to get all HuyActors");
        return huyActorRepository.findAll();
    }

    /**
     * {@code GET  /huy-actors/:id} : get the "id" huyActor.
     *
     * @param id the id of the huyActor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the huyActor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/huy-actors/{id}")
    public ResponseEntity<HuyActor> getHuyActor(@PathVariable Long id) {
        log.debug("REST request to get HuyActor : {}", id);
        Optional<HuyActor> huyActor = huyActorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(huyActor);
    }

    /**
     * {@code DELETE  /huy-actors/:id} : delete the "id" huyActor.
     *
     * @param id the id of the huyActor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/huy-actors/{id}")
    public ResponseEntity<Void> deleteHuyActor(@PathVariable Long id) {
        log.debug("REST request to delete HuyActor : {}", id);
        huyActorRepository.deleteById(id);
        huyActorSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/huy-actors?query=:query} : search for the huyActor corresponding
     * to the query.
     *
     * @param query the query of the huyActor search.
     * @return the result of the search.
     */
    @GetMapping("/_search/huy-actors")
    public List<HuyActor> searchHuyActors(@RequestParam String query) {
        log.debug("REST request to search HuyActors for query {}", query);
        return StreamSupport
            .stream(huyActorSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
