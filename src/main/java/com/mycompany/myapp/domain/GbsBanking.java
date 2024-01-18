package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A GbsBanking.
 */
@Entity
@Table(name = "gbs_banking")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GbsBanking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @NotNull
    @Column(name = "creditor_name", nullable = false)
    private String creditorName;

    @NotNull
    @Column(name = "creditor_account_code", nullable = false)
    private String creditorAccountCode;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "execution_date", nullable = false)
    private LocalDate executionDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GbsBanking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public GbsBanking accountId(Long accountId) {
        this.setAccountId(accountId);
        return this;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCreditorName() {
        return this.creditorName;
    }

    public GbsBanking creditorName(String creditorName) {
        this.setCreditorName(creditorName);
        return this;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorAccountCode() {
        return this.creditorAccountCode;
    }

    public GbsBanking creditorAccountCode(String creditorAccountCode) {
        this.setCreditorAccountCode(creditorAccountCode);
        return this;
    }

    public void setCreditorAccountCode(String creditorAccountCode) {
        this.creditorAccountCode = creditorAccountCode;
    }

    public String getDescription() {
        return this.description;
    }

    public GbsBanking description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return this.currency;
    }

    public GbsBanking currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return this.amount;
    }

    public GbsBanking amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getExecutionDate() {
        return this.executionDate;
    }

    public GbsBanking executionDate(LocalDate executionDate) {
        this.setExecutionDate(executionDate);
        return this;
    }

    public void setExecutionDate(LocalDate executionDate) {
        this.executionDate = executionDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GbsBanking)) {
            return false;
        }
        return getId() != null && getId().equals(((GbsBanking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GbsBanking{" +
            "id=" + getId() +
            ", accountId=" + getAccountId() +
            ", creditorName='" + getCreditorName() + "'" +
            ", creditorAccountCode='" + getCreditorAccountCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", amount=" + getAmount() +
            ", executionDate='" + getExecutionDate() + "'" +
            "}";
    }
}
