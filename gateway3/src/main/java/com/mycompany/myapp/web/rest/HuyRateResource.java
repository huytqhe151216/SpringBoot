package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.HuyRate;
import com.mycompany.myapp.repository.HuyRateRepository;
import com.mycompany.myapp.repository.search.HuyRateSearchRepository;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.HuyRate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HuyRateResource {

    private final Logger log = LoggerFactory.getLogger(HuyRateResource.class);

    private static final String ENTITY_NAME = "huyRate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HuyRateRepository huyRateRepository;

    private final HuyRateSearchRepository huyRateSearchRepository;
    @Autowired
    private UserService userService;

    public HuyRateResource(HuyRateRepository huyRateRepository, HuyRateSearchRepository huyRateSearchRepository) {
        this.huyRateRepository = huyRateRepository;
        this.huyRateSearchRepository = huyRateSearchRepository;
    }

    /**
     * {@code POST  /huy-rates} : Create a new huyRate.
     *
     * @param huyRate the huyRate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new huyRate, or with status {@code 400 (Bad Request)} if the huyRate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/huy-rates")
    public ResponseEntity<HuyRate> createHuyRate(@Valid @RequestBody HuyRate huyRate) throws URISyntaxException {
        log.debug("REST request to save HuyRate : {}", huyRate);
        if (huyRate.getId() != null) {
            throw new BadRequestAlertException("A new huyRate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        huyRate.setUser(userService.getUserWithAuthorities().get());
        HuyRate result = huyRateRepository.save(huyRate);
        huyRateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/huy-rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /huy-rates} : Updates an existing huyRate.
     *
     * @param huyRate the huyRate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated huyRate,
     * or with status {@code 400 (Bad Request)} if the huyRate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the huyRate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/huy-rates")
    public ResponseEntity<HuyRate> updateHuyRate(@Valid @RequestBody HuyRate huyRate) throws URISyntaxException {
        log.debug("REST request to update HuyRate : {}", huyRate);
        if (huyRate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HuyRate result = huyRateRepository.save(huyRate);
        huyRateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, huyRate.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /huy-rates} : get all the huyRates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of huyRates in body.
     */
    @GetMapping("/huy-rates")
    public List<HuyRate> getAllHuyRates() {
        log.debug("REST request to get all HuyRates");
        return huyRateRepository.findAll();
    }

    /**
     * {@code GET  /huy-rates/:id} : get the "id" huyRate.
     *
     * @param id the id of the huyRate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the huyRate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/huy-rates/{id}")
    public ResponseEntity<HuyRate> getHuyRate(@PathVariable Long id) {
        log.debug("REST request to get HuyRate : {}", id);
        Optional<HuyRate> huyRate = huyRateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(huyRate);
    }

    /**
     * {@code DELETE  /huy-rates/:id} : delete the "id" huyRate.
     *
     * @param id the id of the huyRate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/huy-rates/{id}")
    public ResponseEntity<Void> deleteHuyRate(@PathVariable Long id) {
        log.debug("REST request to delete HuyRate : {}", id);
        huyRateRepository.deleteById(id);
        huyRateSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/huy-rates?query=:query} : search for the huyRate corresponding
     * to the query.
     *
     * @param query the query of the huyRate search.
     * @return the result of the search.
     */
    @GetMapping("/_search/huy-rates")
    public List<HuyRate> searchHuyRates(@RequestParam String query) {
        log.debug("REST request to search HuyRates for query {}", query);
        return StreamSupport
            .stream(huyRateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
