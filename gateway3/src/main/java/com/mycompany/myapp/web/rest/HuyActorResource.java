package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.HuyActor;
import com.mycompany.myapp.repository.HuyActorRepository;
import com.mycompany.myapp.repository.search.HuyActorSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.HuyActor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HuyActorResource {

    private final Logger log = LoggerFactory.getLogger(HuyActorResource.class);

    private static final String ENTITY_NAME = "huyActor";

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
        return ResponseEntity.created(new URI("/api/huy-actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /huy-actors} : Updates an existing huyActor.
     *
     * @param huyActor the huyActor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyActor,
     * or with status {@code 400 (Bad Request)} if the huyActor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the huyActor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/huy-actors")
    public ResponseEntity<HuyActor> updateHuyActor(@Valid @RequestBody HuyActor huyActor) throws URISyntaxException {
        log.debug("REST request to update HuyActor : {}", huyActor);
        if (huyActor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HuyActor result = huyActorRepository.save(huyActor);
        huyActorSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyActor.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
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
