package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AccountDisabled;

public interface AccountDisabledService {
    AccountDisabled findByAccountIdAndDisabled(Long accountId, boolean disabled);
}
