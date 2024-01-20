package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AccountDisabled;
import com.mycompany.myapp.repository.AccountDisabledRepository;
import com.mycompany.myapp.service.AccountDisabledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.GbsBanking}.
 */
@Service
@Transactional
public class AccountDisabledServiceImpl implements AccountDisabledService {

    private final Logger log = LoggerFactory.getLogger(AccountDisabledServiceImpl.class);

    private final AccountDisabledRepository accountDisabledRepository;

    public AccountDisabledServiceImpl(AccountDisabledRepository accountDisabledRepository) {
        this.accountDisabledRepository = accountDisabledRepository;
    }

    @Override
    public AccountDisabled findByAccountIdAndDisabled(Long accountId, boolean disabled) {
        log.debug("Request to find by AccountId {} and disabled {}", accountId, disabled);

        return accountDisabledRepository.findByAccountIdAndDisabled(accountId, disabled);
    }
}
