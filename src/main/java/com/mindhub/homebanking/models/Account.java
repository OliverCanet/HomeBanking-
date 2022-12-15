package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native" , strategy = "native")
    private long id;

    private String number;

    private double balance;

    private LocalDateTime creationDate;

    private AccountType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client clients;

    @OneToMany(mappedBy="accounts", fetch= FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    private Boolean isActive;

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction){

        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public Account() {
    }

    public Account(String number, double balance, LocalDateTime creationDate, AccountType type, Boolean isActive) {
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
        this.type = type;
        this.isActive = isActive;
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

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Client getClient() {
        return clients;
    }

    public void setClients(Client clients) {
        this.clients = clients;
    }


    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
