package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.GbsBanking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GbsBanking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GbsBankingRepository extends JpaRepository<GbsBanking, Long> {}
