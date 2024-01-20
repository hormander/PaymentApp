package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.NotNull;

public class GbsBankingAccountCashDTO {

    @NotNull
    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GbsBankingAccountCashDTO{" +
            "value=" + getValue() +
            "}";
    }
}
