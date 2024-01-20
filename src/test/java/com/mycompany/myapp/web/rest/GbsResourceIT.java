package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AccountDisabled;
import com.mycompany.myapp.domain.Accounts;
import com.mycompany.myapp.domain.GbsBanking;
import com.mycompany.myapp.repository.AccountDisabledRepository;
import com.mycompany.myapp.repository.AccountsRepository;
import com.mycompany.myapp.repository.GbsBankingRepository;
import com.mycompany.myapp.service.dto.GbsBankingDTO;
import com.mycompany.myapp.service.mapper.GbsBankingMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GbsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GbsResourceIT {

    private static final Long ENTITY_ACCOUNT_ID = 14537780L;

    private static final Long DEFAULT_ACCOUNT_ID = ENTITY_ACCOUNT_ID;
    private static final Long UPDATED_ACCOUNT_ID = ENTITY_ACCOUNT_ID;

    private static final String DEFAULT_CREDITOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CREDITOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREDITOR_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CREDITOR_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "EUR";
    private static final String UPDATED_CURRENCY = "EUR";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final LocalDate DEFAULT_EXECUTION_DATE = LocalDate.of(2019, 1, 1);
    private static final LocalDate UPDATED_EXECUTION_DATE = LocalDate.of(2019, 12, 1);

    private static final String ENTITY_API_URL = "/apis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GbsBankingRepository gbsBankingRepository;

    @Autowired
    private AccountDisabledRepository accountDisabledRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private GbsBankingMapper gbsBankingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGbsBankingMockMvc;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GbsBanking createEntity(EntityManager em, LocalDate executionDate) {
        return new GbsBanking()
            .accountId(DEFAULT_ACCOUNT_ID)
            .creditorName(DEFAULT_CREDITOR_NAME)
            .creditorAccountCode(DEFAULT_CREDITOR_ACCOUNT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .currency(DEFAULT_CURRENCY)
            .amount(DEFAULT_AMOUNT)
            .executionDate(executionDate);
    }

    public static AccountDisabled createAccountDisabledEntity(EntityManager em) {
        return new AccountDisabled().accountId(DEFAULT_ACCOUNT_ID).disabled(true).condition("BP049");
    }

    public static Accounts createAccount(Long accountId) {
        return new Accounts().accountId(accountId);
    }

    @Test
    @Transactional
    void getGbsBankingAccountCashByAccountId() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null

        accountsRepository.save(createAccount(DEFAULT_ACCOUNT_ID));

        Double value = gbsBankingRepository
            .findAllByAccountId(ENTITY_ACCOUNT_ID)
            .stream()
            .reduce(0.0, (acc, e) -> acc + e.getAmount(), Double::sum);

        GbsBanking newGbsBanking = gbsBankingRepository.save(createEntity(em, LocalDate.of(2019, 1, 1)));

        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeTest + 1);

        restGbsBankingMockMvc
            .perform(get(ENTITY_API_URL + "/gbs-banking-account-cash-v4.0/{id}", ENTITY_ACCOUNT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.value").value(value + newGbsBanking.getAmount()));

        accountsRepository.deleteByAccountId(DEFAULT_ACCOUNT_ID);

        gbsBankingRepository.deleteById(newGbsBanking.getId());

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getGbsBankingAccountCashByAccountIdNotFound() throws Exception {
        long leftLimit = 1L;
        long rightLimit = 10L;
        long generatedAccountId = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));

        restGbsBankingMockMvc
            .perform(get(ENTITY_API_URL + "/gbs-banking-account-cash-v4.0/{id}", generatedAccountId))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void findAllByAccountIdAndExecutionDateBetween() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null

        accountsRepository.save(createAccount(DEFAULT_ACCOUNT_ID));

        GbsBanking newGbsBanking = gbsBankingRepository.save(createEntity(em, LocalDate.of(2019, 1, 1)));
        GbsBanking newGbsBanking2 = gbsBankingRepository.save(createEntity(em, LocalDate.of(2019, 12, 1)));
        GbsBanking newGbsBanking3 = gbsBankingRepository.save(createEntity(em, LocalDate.of(2024, 12, 1)));

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeTest + 3);

        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2019, 12, 31);

        restGbsBankingMockMvc
            .perform(
                get(
                    ENTITY_API_URL +
                    "/gbs-banking-account-cash-v4.0?accountId={id}&fromAccountingDate={fromAccountingDate}&toAccountingDate={toAccountingDate}",
                    ENTITY_ACCOUNT_ID,
                    from,
                    to
                )
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.*", hasSize(2)));

        gbsBankingRepository.deleteById(newGbsBanking.getId());
        gbsBankingRepository.deleteById(newGbsBanking2.getId());
        gbsBankingRepository.deleteById(newGbsBanking3.getId());

        accountsRepository.deleteByAccountId(DEFAULT_ACCOUNT_ID);

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void findAllByAccountIdAndExecutionDateBetweenAccountIdNotFound() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null

        long leftLimit = 1L;
        long rightLimit = 10L;
        long generatedAccountId = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));

        LocalDate from = LocalDate.of(2018, 1, 1);
        LocalDate to = LocalDate.of(2019, 12, 31);

        restGbsBankingMockMvc
            .perform(
                get(
                    ENTITY_API_URL +
                    "/gbs-banking-account-cash-v4.0?accountId={id}&fromAccountingDate={fromAccountingDate}&toAccountingDate={toAccountingDate}",
                    generatedAccountId,
                    from,
                    to
                )
            )
            .andExpect(status().isNotFound());

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void findAllByAccountIdAndExecutionDateBetweenZeroRecords() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null

        accountsRepository.save(createAccount(DEFAULT_ACCOUNT_ID));

        GbsBanking newGbsBanking = gbsBankingRepository.save(createEntity(em, LocalDate.of(2019, 1, 1)));
        GbsBanking newGbsBanking2 = gbsBankingRepository.save(createEntity(em, LocalDate.of(2019, 12, 1)));
        GbsBanking newGbsBanking3 = gbsBankingRepository.save(createEntity(em, LocalDate.of(2024, 12, 1)));

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeTest + 3);

        LocalDate from = LocalDate.of(2020, 1, 1);
        LocalDate to = LocalDate.of(2020, 12, 31);

        restGbsBankingMockMvc
            .perform(
                get(
                    ENTITY_API_URL +
                    "/gbs-banking-account-cash-v4.0?accountId={id}&fromAccountingDate={fromAccountingDate}&toAccountingDate={toAccountingDate}",
                    ENTITY_ACCOUNT_ID,
                    from,
                    to
                )
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.*", hasSize(0)));

        gbsBankingRepository.deleteById(newGbsBanking.getId());
        gbsBankingRepository.deleteById(newGbsBanking2.getId());
        gbsBankingRepository.deleteById(newGbsBanking3.getId());

        accountsRepository.deleteByAccountId(DEFAULT_ACCOUNT_ID);

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void createGbsBankingPaymentsMoneyTransfersSuccess() throws Exception {
        int databaseSizeBeforeCreate = gbsBankingRepository.findAll().size();

        accountsRepository.save(createAccount(DEFAULT_ACCOUNT_ID));

        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(createEntity(em, LocalDate.of(2019, 1, 1)));

        MvcResult result = restGbsBankingMockMvc
            .perform(
                post(ENTITY_API_URL + "/gbs-banking-payments-moneyTransfers-v4.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isCreated())
            .andReturn();

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeCreate + 1);

        String content = result.getResponse().getContentAsString();

        Integer accountId = JsonPath.read(content, "$.accountId");
        assertThat((long) accountId).isEqualTo(gbsBankingDTO.getAccountId());

        String creditorName = JsonPath.read(content, "$.creditorName");
        assertThat(creditorName).isEqualTo(gbsBankingDTO.getCreditorName());

        String creditorAccountCode = JsonPath.read(content, "$.creditorAccountCode");
        assertThat(creditorAccountCode).isEqualTo(gbsBankingDTO.getCreditorAccountCode());

        String description = JsonPath.read(content, "$.description");
        assertThat(description).isEqualTo(gbsBankingDTO.getDescription());

        String currency = JsonPath.read(content, "$.currency");
        assertThat(currency).isEqualTo(gbsBankingDTO.getCurrency());

        Double amount = JsonPath.read(content, "$.amount");
        assertThat(amount).isEqualTo(gbsBankingDTO.getAmount());

        String executionDate = JsonPath.read(content, "$.executionDate");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        assertThat(LocalDate.parse(executionDate, dtf)).isEqualTo(gbsBankingDTO.getExecutionDate());

        Integer id = JsonPath.read(content, "$.id");

        gbsBankingRepository.deleteById((long) id);

        accountsRepository.deleteByAccountId(DEFAULT_ACCOUNT_ID);

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void createGbsBankingPaymentsMoneyTransfersForbidden() throws Exception {
        int databaseSizeBeforeCreate = gbsBankingRepository.findAll().size();

        int accountDisableSizeBeforeCreate = accountDisabledRepository.findAll().size();

        accountsRepository.save(createAccount(DEFAULT_ACCOUNT_ID));

        AccountDisabled ad = createAccountDisabledEntity(em);

        AccountDisabled accountDisabled = accountDisabledRepository.save(ad);

        assertThat(accountDisabledRepository.findAll()).hasSize(accountDisableSizeBeforeCreate + 1);

        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(createEntity(em, LocalDate.of(2019, 1, 1)));

        MvcResult result = restGbsBankingMockMvc
            .perform(
                post(ENTITY_API_URL + "/gbs-banking-payments-moneyTransfers-v4.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isForbidden())
            .andReturn();

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeCreate);

        String content = result.getResponse().getContentAsString();

        String code = JsonPath.read(content, "$.code");
        assertThat(code).isEqualTo("API000");

        String description = JsonPath.read(content, "$.description");
        assertThat(description).contains("BP049");

        accountDisabledRepository.deleteById(accountDisabled.getId());

        accountsRepository.deleteByAccountId(DEFAULT_ACCOUNT_ID);

        assertThat(accountDisabledRepository.findAll()).hasSize(accountDisableSizeBeforeCreate);
    }

    @Test
    @Transactional
    void createGbsBankingPaymentsMoneyTransfersAccountIdNotFound() throws Exception {
        int databaseSizeBeforeCreate = gbsBankingRepository.findAll().size();

        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(createEntity(em, LocalDate.of(2019, 1, 1)));

        long leftLimit = 1L;
        long rightLimit = 10L;
        long generatedAccountId = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));

        gbsBankingDTO.setAccountId(generatedAccountId);

        restGbsBankingMockMvc
            .perform(
                post(ENTITY_API_URL + "/gbs-banking-payments-moneyTransfers-v4.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isNotFound());

        assertThat(gbsBankingRepository.findAll()).hasSize(databaseSizeBeforeCreate);
    }
}
