package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.GbsBanking;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GbsBanking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GbsBankingRepository extends JpaRepository<GbsBanking, Long> {
    GbsBanking findOneByAccountId(Long accountId);

    List<GbsBanking> findAllByAccountId(Long accountId);

    List<GbsBanking> findAllByAccountIdAndExecutionDateBetween(Long accountId, LocalDate from, LocalDate to);
}
