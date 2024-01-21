package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.GbsBankingService;
import com.mycompany.myapp.service.dto.GbsBankingAccountCashDTO;
import com.mycompany.myapp.service.dto.GbsBankingDTO;
import com.mycompany.myapp.web.rest.errors.AccountIdNotFoundException;
import com.mycompany.myapp.web.rest.errors.Api000Exception;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.DatesIntervalPreconditionFailed;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/apis")
public class GbsResource {

    private final Logger log = LoggerFactory.getLogger(GbsBankingResource.class);

    private static final String ENTITY_NAME = "gbsBanking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GbsBankingService gbsBankingService;

    public GbsResource(GbsBankingService gbsBankingService) {
        this.gbsBankingService = gbsBankingService;
    }

    /**
     * {@code GET  /gbs-bankings/:id} : get the "id" gbsBanking.
     *
     * @param id the id of the gbsBankingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gbsBankingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gbs-banking-account-cash-v4.0/{accountId}")
    public ResponseEntity<GbsBankingAccountCashDTO> getGbsBankingAccountCash(@PathVariable("accountId") Long accountId)
        throws AccountIdNotFoundException {
        log.debug("REST request to get value of cash for accountId : {}", accountId);

        GbsBankingAccountCashDTO result = gbsBankingService.getGbsBankingAccountCash(accountId);

        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /gbs-bankings/:id} : get the "id" gbsBanking.
     *
     * @param id the id of the gbsBankingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gbsBankingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gbs-banking-account-cash-v4.0")
    public ResponseEntity<List<GbsBankingDTO>> findAllByAccountIdAndExecutionDateBetween(
        @RequestParam("accountId") Long accountId,
        @RequestParam("fromAccountingDate") LocalDate fromAccountingDate,
        @RequestParam("toAccountingDate") LocalDate toAccountingDate
    ) throws AccountIdNotFoundException, DatesIntervalPreconditionFailed {
        log.debug("REST request to find all GbsBankingAccountCash for accountId : {}", accountId);

        if (toAccountingDate.isBefore(fromAccountingDate)) {
            String message = String.format("'To date' {} is less than 'from date' {}", toAccountingDate, fromAccountingDate);

            log.warn(message);

            throw new DatesIntervalPreconditionFailed(message);
        }

        List<GbsBankingDTO> result = gbsBankingService.findAllByAccountIdAndExecutionDateBetween(
            accountId,
            fromAccountingDate,
            toAccountingDate
        );

        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code POST  /gbs-bankings} : Create a new gbsBanking.
     *
     * @param gbsBankingDTO the gbsBankingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gbsBankingDTO, or with status {@code 400 (Bad Request)} if the gbsBanking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gbs-banking-payments-moneyTransfers-v4.0")
    public ResponseEntity<GbsBankingDTO> createGbsBankingPaymentsMoneyTransfers(@Valid @RequestBody GbsBankingDTO gbsBankingDTO)
        throws URISyntaxException, Api000Exception, AccountIdNotFoundException {
        log.debug("REST request to save GbsBanking : {}", gbsBankingDTO);

        if (gbsBankingDTO.getId() != null) {
            throw new BadRequestAlertException("A new gbsBanking cannot already have an ID", ENTITY_NAME, "idexists");
        }

        GbsBankingDTO result = gbsBankingService.save(gbsBankingDTO);

        return ResponseEntity
            .created(new URI("/apis/gbs-banking-payments-moneyTransfers-v4.0/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
}
