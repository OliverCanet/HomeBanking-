package com.mindhub.homebanking.dtos;

import java.util.List;

public class LoanApplicationDTO {

    private Long idLoan;

    private double amount;

    private List<Integer> payments;

    private String accountDestiny;



    public Long getIdLoan() {
        return idLoan;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }

    public void setAccountDestiny(String accountDestiny) {
        this.accountDestiny = accountDestiny;
    }
}
