package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.GbsBankingAccountCashDTO;
import com.mycompany.myapp.service.dto.GbsBankingDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.GbsBanking}.
 */
public interface GbsBankingService {
    /**
     * Save a gbsBanking.
     *
     * @param gbsBankingDTO the entity to save.
     * @return the persisted entity.
     */
    GbsBankingDTO save(GbsBankingDTO gbsBankingDTO);

    /**
     * Updates a gbsBanking.
     *
     * @param gbsBankingDTO the entity to update.
     * @return the persisted entity.
     */
    GbsBankingDTO update(GbsBankingDTO gbsBankingDTO);

    /**
     * Partially updates a gbsBanking.
     *
     * @param gbsBankingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GbsBankingDTO> partialUpdate(GbsBankingDTO gbsBankingDTO);

    /**
     * Get all the gbsBankings.
     *
     * @return the list of entities.
     */
    List<GbsBankingDTO> findAll();

    /**
     * Get the "id" gbsBanking.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GbsBankingDTO> findOne(Long id);

    /**
     * Delete the "id" gbsBanking.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    GbsBankingAccountCashDTO getGbsBankingAccountCash(Long accountId);

    List<GbsBankingDTO> findAllByAccountIdAndExecutionDateBetween(Long accountId, LocalDate fromAccountingDate, LocalDate toAccountingDate);
}
