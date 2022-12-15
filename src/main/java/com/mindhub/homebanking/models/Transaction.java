package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native" , strategy = "native")
    private long id;



    private double amount;

    private String description;

    private LocalDateTime date;


    private TransactionType type;



    public Transaction() {
    }

    public Transaction(double amount, String description, LocalDateTime date, TransactionType type ) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        //this.accountBalance = accountBalance;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="accounts_id")
    private Account accounts;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }



    public Account getAccount() {
        return accounts;
    }

    public void setAccount(Account account) {
        this.accounts = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", type=" + type +
                ", account=" + accounts +
                '}';
    }
}

