package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.HuyRate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HuyRate} entity.
 */
public interface HuyRateSearchRepository extends ElasticsearchRepository<HuyRate, Long> {
}
