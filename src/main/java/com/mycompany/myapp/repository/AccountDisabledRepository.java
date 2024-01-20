package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccountDisabled;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountDisabled entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountDisabledRepository extends JpaRepository<AccountDisabled, Long> {
    AccountDisabled findByAccountIdAndDisabled(Long accountId, boolean disabled);
}
