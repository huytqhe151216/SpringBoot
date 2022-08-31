package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.HuyMovie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HuyMovie} entity.
 */
public interface HuyMovieSearchRepository extends ElasticsearchRepository<HuyMovie, Long> {
}
