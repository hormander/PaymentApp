package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.GbsBanking;
import com.mycompany.myapp.repository.GbsBankingRepository;
import com.mycompany.myapp.service.dto.GbsBankingDTO;
import com.mycompany.myapp.service.mapper.GbsBankingMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GbsBankingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GbsBankingResourceIT {

    private static final Long DEFAULT_ACCOUNT_ID = 1L;
    private static final Long UPDATED_ACCOUNT_ID = 2L;

    private static final String DEFAULT_CREDITOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CREDITOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CREDITOR_ACCOUNT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CREDITOR_ACCOUNT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final LocalDate DEFAULT_EXECUTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXECUTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/gbs-bankings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GbsBankingRepository gbsBankingRepository;

    @Autowired
    private GbsBankingMapper gbsBankingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGbsBankingMockMvc;

    private GbsBanking gbsBanking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GbsBanking createEntity(EntityManager em) {
        GbsBanking gbsBanking = new GbsBanking()
            .accountId(DEFAULT_ACCOUNT_ID)
            .creditorName(DEFAULT_CREDITOR_NAME)
            .creditorAccountCode(DEFAULT_CREDITOR_ACCOUNT_CODE)
            .description(DEFAULT_DESCRIPTION)
            .currency(DEFAULT_CURRENCY)
            .amount(DEFAULT_AMOUNT)
            .executionDate(DEFAULT_EXECUTION_DATE);
        return gbsBanking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GbsBanking createUpdatedEntity(EntityManager em) {
        GbsBanking gbsBanking = new GbsBanking()
            .accountId(UPDATED_ACCOUNT_ID)
            .creditorName(UPDATED_CREDITOR_NAME)
            .creditorAccountCode(UPDATED_CREDITOR_ACCOUNT_CODE)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .amount(UPDATED_AMOUNT)
            .executionDate(UPDATED_EXECUTION_DATE);
        return gbsBanking;
    }

    @BeforeEach
    public void initTest() {
        gbsBanking = createEntity(em);
    }

    @Test
    @Transactional
    void createGbsBanking() throws Exception {
        int databaseSizeBeforeCreate = gbsBankingRepository.findAll().size();
        // Create the GbsBanking
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);
        restGbsBankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isCreated());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeCreate + 1);
        GbsBanking testGbsBanking = gbsBankingList.get(gbsBankingList.size() - 1);
        assertThat(testGbsBanking.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testGbsBanking.getCreditorName()).isEqualTo(DEFAULT_CREDITOR_NAME);
        assertThat(testGbsBanking.getCreditorAccountCode()).isEqualTo(DEFAULT_CREDITOR_ACCOUNT_CODE);
        assertThat(testGbsBanking.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGbsBanking.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testGbsBanking.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testGbsBanking.getExecutionDate()).isEqualTo(DEFAULT_EXECUTION_DATE);
    }

    @Test
    @Transactional
    void createGbsBankingWithExistingId() throws Exception {
        // Create the GbsBanking with an existing ID
        gbsBanking.setId(1L);
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        int databaseSizeBeforeCreate = gbsBankingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGbsBankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAccountIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null
        gbsBanking.setAccountId(null);

        // Create the GbsBanking, which fails.
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        restGbsBankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isBadRequest());

        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null
        gbsBanking.setCreditorName(null);

        // Create the GbsBanking, which fails.
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        restGbsBankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isBadRequest());

        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditorAccountCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null
        gbsBanking.setCreditorAccountCode(null);

        // Create the GbsBanking, which fails.
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        restGbsBankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isBadRequest());

        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null
        gbsBanking.setCurrency(null);

        // Create the GbsBanking, which fails.
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        restGbsBankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isBadRequest());

        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null
        gbsBanking.setAmount(null);

        // Create the GbsBanking, which fails.
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        restGbsBankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isBadRequest());

        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExecutionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gbsBankingRepository.findAll().size();
        // set the field null
        gbsBanking.setExecutionDate(null);

        // Create the GbsBanking, which fails.
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        restGbsBankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isBadRequest());

        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGbsBankings() throws Exception {
        // Initialize the database
        gbsBankingRepository.saveAndFlush(gbsBanking);

        // Get all the gbsBankingList
        restGbsBankingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gbsBanking.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountId").value(hasItem(DEFAULT_ACCOUNT_ID.intValue())))
            .andExpect(jsonPath("$.[*].creditorName").value(hasItem(DEFAULT_CREDITOR_NAME)))
            .andExpect(jsonPath("$.[*].creditorAccountCode").value(hasItem(DEFAULT_CREDITOR_ACCOUNT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].executionDate").value(hasItem(DEFAULT_EXECUTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getGbsBanking() throws Exception {
        // Initialize the database
        gbsBankingRepository.saveAndFlush(gbsBanking);

        // Get the gbsBanking
        restGbsBankingMockMvc
            .perform(get(ENTITY_API_URL_ID, gbsBanking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gbsBanking.getId().intValue()))
            .andExpect(jsonPath("$.accountId").value(DEFAULT_ACCOUNT_ID.intValue()))
            .andExpect(jsonPath("$.creditorName").value(DEFAULT_CREDITOR_NAME))
            .andExpect(jsonPath("$.creditorAccountCode").value(DEFAULT_CREDITOR_ACCOUNT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.executionDate").value(DEFAULT_EXECUTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGbsBanking() throws Exception {
        // Get the gbsBanking
        restGbsBankingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGbsBanking() throws Exception {
        // Initialize the database
        gbsBankingRepository.saveAndFlush(gbsBanking);

        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();

        // Update the gbsBanking
        GbsBanking updatedGbsBanking = gbsBankingRepository.findById(gbsBanking.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGbsBanking are not directly saved in db
        em.detach(updatedGbsBanking);
        updatedGbsBanking
            .accountId(UPDATED_ACCOUNT_ID)
            .creditorName(UPDATED_CREDITOR_NAME)
            .creditorAccountCode(UPDATED_CREDITOR_ACCOUNT_CODE)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .amount(UPDATED_AMOUNT)
            .executionDate(UPDATED_EXECUTION_DATE);
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(updatedGbsBanking);

        restGbsBankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gbsBankingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isOk());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
        GbsBanking testGbsBanking = gbsBankingList.get(gbsBankingList.size() - 1);
        assertThat(testGbsBanking.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testGbsBanking.getCreditorName()).isEqualTo(UPDATED_CREDITOR_NAME);
        assertThat(testGbsBanking.getCreditorAccountCode()).isEqualTo(UPDATED_CREDITOR_ACCOUNT_CODE);
        assertThat(testGbsBanking.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGbsBanking.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testGbsBanking.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testGbsBanking.getExecutionDate()).isEqualTo(UPDATED_EXECUTION_DATE);
    }

    @Test
    @Transactional
    void putNonExistingGbsBanking() throws Exception {
        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();
        gbsBanking.setId(longCount.incrementAndGet());

        // Create the GbsBanking
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGbsBankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gbsBankingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGbsBanking() throws Exception {
        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();
        gbsBanking.setId(longCount.incrementAndGet());

        // Create the GbsBanking
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGbsBankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGbsBanking() throws Exception {
        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();
        gbsBanking.setId(longCount.incrementAndGet());

        // Create the GbsBanking
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGbsBankingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGbsBankingWithPatch() throws Exception {
        // Initialize the database
        gbsBankingRepository.saveAndFlush(gbsBanking);

        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();

        // Update the gbsBanking using partial update
        GbsBanking partialUpdatedGbsBanking = new GbsBanking();
        partialUpdatedGbsBanking.setId(gbsBanking.getId());

        partialUpdatedGbsBanking
            .creditorName(UPDATED_CREDITOR_NAME)
            .creditorAccountCode(UPDATED_CREDITOR_ACCOUNT_CODE)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .amount(UPDATED_AMOUNT)
            .executionDate(UPDATED_EXECUTION_DATE);

        restGbsBankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGbsBanking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGbsBanking))
            )
            .andExpect(status().isOk());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
        GbsBanking testGbsBanking = gbsBankingList.get(gbsBankingList.size() - 1);
        assertThat(testGbsBanking.getAccountId()).isEqualTo(DEFAULT_ACCOUNT_ID);
        assertThat(testGbsBanking.getCreditorName()).isEqualTo(UPDATED_CREDITOR_NAME);
        assertThat(testGbsBanking.getCreditorAccountCode()).isEqualTo(UPDATED_CREDITOR_ACCOUNT_CODE);
        assertThat(testGbsBanking.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGbsBanking.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testGbsBanking.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testGbsBanking.getExecutionDate()).isEqualTo(UPDATED_EXECUTION_DATE);
    }

    @Test
    @Transactional
    void fullUpdateGbsBankingWithPatch() throws Exception {
        // Initialize the database
        gbsBankingRepository.saveAndFlush(gbsBanking);

        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();

        // Update the gbsBanking using partial update
        GbsBanking partialUpdatedGbsBanking = new GbsBanking();
        partialUpdatedGbsBanking.setId(gbsBanking.getId());

        partialUpdatedGbsBanking
            .accountId(UPDATED_ACCOUNT_ID)
            .creditorName(UPDATED_CREDITOR_NAME)
            .creditorAccountCode(UPDATED_CREDITOR_ACCOUNT_CODE)
            .description(UPDATED_DESCRIPTION)
            .currency(UPDATED_CURRENCY)
            .amount(UPDATED_AMOUNT)
            .executionDate(UPDATED_EXECUTION_DATE);

        restGbsBankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGbsBanking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGbsBanking))
            )
            .andExpect(status().isOk());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
        GbsBanking testGbsBanking = gbsBankingList.get(gbsBankingList.size() - 1);
        assertThat(testGbsBanking.getAccountId()).isEqualTo(UPDATED_ACCOUNT_ID);
        assertThat(testGbsBanking.getCreditorName()).isEqualTo(UPDATED_CREDITOR_NAME);
        assertThat(testGbsBanking.getCreditorAccountCode()).isEqualTo(UPDATED_CREDITOR_ACCOUNT_CODE);
        assertThat(testGbsBanking.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGbsBanking.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testGbsBanking.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testGbsBanking.getExecutionDate()).isEqualTo(UPDATED_EXECUTION_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingGbsBanking() throws Exception {
        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();
        gbsBanking.setId(longCount.incrementAndGet());

        // Create the GbsBanking
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGbsBankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gbsBankingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGbsBanking() throws Exception {
        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();
        gbsBanking.setId(longCount.incrementAndGet());

        // Create the GbsBanking
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGbsBankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGbsBanking() throws Exception {
        int databaseSizeBeforeUpdate = gbsBankingRepository.findAll().size();
        gbsBanking.setId(longCount.incrementAndGet());

        // Create the GbsBanking
        GbsBankingDTO gbsBankingDTO = gbsBankingMapper.toDto(gbsBanking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGbsBankingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gbsBankingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GbsBanking in the database
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGbsBanking() throws Exception {
        // Initialize the database
        gbsBankingRepository.saveAndFlush(gbsBanking);

        int databaseSizeBeforeDelete = gbsBankingRepository.findAll().size();

        // Delete the gbsBanking
        restGbsBankingMockMvc
            .perform(delete(ENTITY_API_URL_ID, gbsBanking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GbsBanking> gbsBankingList = gbsBankingRepository.findAll();
        assertThat(gbsBankingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
