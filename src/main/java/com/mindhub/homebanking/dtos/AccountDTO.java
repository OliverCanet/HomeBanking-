package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private long id;

    private String number;

    private double balance;

    private LocalDateTime creationDate;

    private AccountType type;

    private Set<TransactionDTO> transactions;

    private Boolean isActive;


    public AccountDTO(Account account) {
        this.id = account.getId();
        this.isActive = account.getActive();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.type = account.getType();
        this.creationDate = account.getCreationDate();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public AccountType getType() {
        return type;
    }

    public Boolean getActive() {
        return isActive;
    }
}
