package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.HuyActor;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HuyActor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HuyActorRepository extends JpaRepository<HuyActor, Long> {
}
