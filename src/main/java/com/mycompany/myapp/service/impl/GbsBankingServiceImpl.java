package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AccountDisabled;
import com.mycompany.myapp.domain.GbsBanking;
import com.mycompany.myapp.repository.AccountDisabledRepository;
import com.mycompany.myapp.repository.AccountsRepository;
import com.mycompany.myapp.repository.GbsBankingRepository;
import com.mycompany.myapp.service.GbsBankingService;
import com.mycompany.myapp.service.dto.GbsBankingAccountCashDTO;
import com.mycompany.myapp.service.dto.GbsBankingDTO;
import com.mycompany.myapp.service.mapper.GbsBankingMapper;
import com.mycompany.myapp.web.rest.errors.AccountIdNotFoundException;
import com.mycompany.myapp.web.rest.errors.Api000Exception;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.GbsBanking}.
 */
@Service
@Transactional
public class GbsBankingServiceImpl implements GbsBankingService {

    private final Logger log = LoggerFactory.getLogger(GbsBankingServiceImpl.class);

    private final AccountsRepository accountsRepository;

    private final AccountDisabledRepository accountDisabledRepository;

    private final GbsBankingRepository gbsBankingRepository;

    private final GbsBankingMapper gbsBankingMapper;

    public GbsBankingServiceImpl(
        AccountsRepository accountsRepository,
        AccountDisabledRepository accountDisabledRepository,
        GbsBankingRepository gbsBankingRepository,
        GbsBankingMapper gbsBankingMapper
    ) {
        this.accountsRepository = accountsRepository;
        this.accountDisabledRepository = accountDisabledRepository;
        this.gbsBankingRepository = gbsBankingRepository;
        this.gbsBankingMapper = gbsBankingMapper;
    }

    @Override
    public GbsBankingDTO update(GbsBankingDTO gbsBankingDTO) {
        log.debug("Request to update GbsBanking : {}", gbsBankingDTO);
        GbsBanking gbsBanking = gbsBankingMapper.toEntity(gbsBankingDTO);
        gbsBanking = gbsBankingRepository.save(gbsBanking);
        return gbsBankingMapper.toDto(gbsBanking);
    }

    @Override
    public Optional<GbsBankingDTO> partialUpdate(GbsBankingDTO gbsBankingDTO) {
        log.debug("Request to partially update GbsBanking : {}", gbsBankingDTO);

        return gbsBankingRepository
            .findById(gbsBankingDTO.getId())
            .map(existingGbsBanking -> {
                gbsBankingMapper.partialUpdate(existingGbsBanking, gbsBankingDTO);

                return existingGbsBanking;
            })
            .map(gbsBankingRepository::save)
            .map(gbsBankingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GbsBankingDTO> findAll() {
        log.debug("Request to get all GbsBankings");
        return gbsBankingRepository.findAll().stream().map(gbsBankingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GbsBankingDTO> findOne(Long id) {
        log.debug("Request to get GbsBanking : {}", id);
        return gbsBankingRepository.findById(id).map(gbsBankingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GbsBanking : {}", id);
        gbsBankingRepository.deleteById(id);
    }

    @Override
    public GbsBankingDTO save(GbsBankingDTO gbsBankingDTO) {
        log.debug("Request to save GbsBanking : {}", gbsBankingDTO);

        if (accountsRepository.findOneByAccountId(gbsBankingDTO.getAccountId()) == null) {
            String message = String.format("Account Id %s not found", gbsBankingDTO.getAccountId());

            log.warn(message);

            throw new AccountIdNotFoundException(message);
        }

        AccountDisabled accountDisabled = accountDisabledRepository.findByAccountIdAndDisabled(gbsBankingDTO.getAccountId(), true);
        if (accountDisabled != null) {
            String message = String.format(
                "Errore tecnico. La condizione %s non è prevista per il conto %s",
                accountDisabled.getCondition(),
                accountDisabled.getAccountId()
            );

            log.warn(message);

            throw new Api000Exception(message);
        }

        GbsBanking gbsBanking = gbsBankingMapper.toEntity(gbsBankingDTO);

        gbsBanking = gbsBankingRepository.save(gbsBanking);

        return gbsBankingMapper.toDto(gbsBanking);
    }

    @Override
    @Transactional(readOnly = true)
    public GbsBankingAccountCashDTO getGbsBankingAccountCash(Long accountId) {
        log.debug("Request to get value of cash for accountId : {}", accountId);

        if (accountsRepository.findOneByAccountId(accountId) == null) {
            String message = String.format("Account Id %s not found", accountId);

            log.warn(message);

            throw new AccountIdNotFoundException(message);
        }

        Double cash = gbsBankingRepository.findAllByAccountId(accountId).stream().reduce(0.0, (acc, u) -> acc + u.getAmount(), Double::sum);

        GbsBankingAccountCashDTO dto = new GbsBankingAccountCashDTO();
        dto.setValue(cash);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GbsBankingDTO> findAllByAccountIdAndExecutionDateBetween(
        Long accountId,
        LocalDate fromAccountingDate,
        LocalDate toAccountingDate
    ) {
        log.debug("Request to find all GbsBankingAccountCash for accountId : {}", accountId);

        if (accountsRepository.findOneByAccountId(accountId) == null) {
            String message = String.format("Account Id %s not found", accountId);

            log.warn(message);

            throw new AccountIdNotFoundException(message);
        }

        List<GbsBanking> result = gbsBankingRepository.findAllByAccountIdAndExecutionDateBetween(
            accountId,
            fromAccountingDate,
            toAccountingDate
        );

        return result.stream().map(gbsBankingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
