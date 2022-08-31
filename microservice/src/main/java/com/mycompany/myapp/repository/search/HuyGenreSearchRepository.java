package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.HuyGenre;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HuyGenre} entity.
 */
public interface HuyGenreSearchRepository extends ElasticsearchRepository<HuyGenre, Long> {}
