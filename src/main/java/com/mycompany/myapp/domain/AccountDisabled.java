package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A GbsBanking.
 */
@Entity
@Table(name = "account_disabled")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountDisabled implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @NotNull
    @Column(name = "disabled", nullable = false)
    private Boolean disabled;

    @NotNull
    @Column(name = "condition", nullable = true)
    private String condition;

    public Long getId() {
        return this.id;
    }

    public AccountDisabled id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public AccountDisabled accountId(Long accountId) {
        this.setAccountId(accountId);
        return this;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public AccountDisabled disabled(Boolean disabled) {
        this.setDisabled(disabled);
        return this;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getCondition() {
        return this.condition;
    }

    public AccountDisabled condition(String condition) {
        this.setCondition(condition);
        return this;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
        return "AccountDisabled{" +
            "id=" + getId() +
            ", accountId=" + getAccountId() +
            ", disabled=" + getDisabled() +
            ", condition='" + getCondition() + "'" +
            "}";
    }
}
