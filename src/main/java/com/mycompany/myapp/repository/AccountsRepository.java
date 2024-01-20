package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccountDisabled;
import com.mycompany.myapp.domain.Accounts;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccountDisabled entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Accounts findOneByAccountId(Long accountId);

    void deleteByAccountId(Long accountId);
}
