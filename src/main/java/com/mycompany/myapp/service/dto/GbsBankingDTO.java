package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.GbsBanking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GbsBankingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long accountId;

    @NotNull
    private String creditorName;

    @NotNull
    private String creditorAccountCode;

    private String description;

    @NotNull
    private String currency;

    @NotNull
    private Double amount;

    @NotNull
    private LocalDate executionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public String getCreditorAccountCode() {
        return creditorAccountCode;
    }

    public void setCreditorAccountCode(String creditorAccountCode) {
        this.creditorAccountCode = creditorAccountCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDate executionDate) {
        this.executionDate = executionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GbsBankingDTO)) {
            return false;
        }

        GbsBankingDTO gbsBankingDTO = (GbsBankingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gbsBankingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GbsBankingDTO{" +
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
