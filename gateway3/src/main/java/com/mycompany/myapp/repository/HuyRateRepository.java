package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.HuyRate;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HuyRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HuyRateRepository extends JpaRepository<HuyRate, Long> {
}
