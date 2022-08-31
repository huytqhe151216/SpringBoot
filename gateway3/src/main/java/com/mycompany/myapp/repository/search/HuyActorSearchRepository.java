package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.HuyActor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HuyActor} entity.
 */
public interface HuyActorSearchRepository extends ElasticsearchRepository<HuyActor, Long> {
}
