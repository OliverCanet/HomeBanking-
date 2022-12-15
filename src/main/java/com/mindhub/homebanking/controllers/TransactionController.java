package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {



    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction (

            Authentication authentication, @RequestParam double amount, @RequestParam String description, @RequestParam String accountO, @RequestParam String accountD,@RequestParam double accountBalance)

    {

        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account accountOrigin = accountService.findByNumber(accountO);
        Account accountDestin = accountService.findByNumber(accountD);
        Set<Account> accountExist = clientCurrent.getAccounts().stream().filter(account -> account.getNumber().equals(accountO)).collect(Collectors.toSet());



        if (amount <= 0) {
            return new ResponseEntity<>("Missing Amount", HttpStatus.EXPECTATION_FAILED);
        }
        if (description.isEmpty()) {
            return new ResponseEntity<>("Missing Description", HttpStatus.FORBIDDEN);
        }
        if (accountO.isEmpty() ) {
            return new ResponseEntity<>("Missing Origin Account", HttpStatus.FORBIDDEN);
        }
        if (accountD.isEmpty()) {
            return new ResponseEntity<>("Missing Destiny Account", HttpStatus.FORBIDDEN);
        }

        if (accountO.equals(accountD)){
            return new ResponseEntity<>("Origin Account cant be the same as Destiny Account", HttpStatus.FORBIDDEN);
        }
        if(accountExist.isEmpty()){
            return new ResponseEntity<>("Your origin account doesent exist", HttpStatus.FORBIDDEN);
        }

        if(accountDestin == null){
            return new ResponseEntity<>("Your Destiny Account doesent exist", HttpStatus.FORBIDDEN);
        }

        if(accountOrigin.getBalance() < amount){
            return new ResponseEntity<>("Not enough balance", HttpStatus.FORBIDDEN);
        }




        Transaction transactionOrigin = new Transaction( amount, description + " " + accountDestin.getNumber(), LocalDateTime.now(),TransactionType.DEBIT);
        Transaction transactionDestin = new Transaction( amount, description + " " + accountOrigin.getNumber(), LocalDateTime.now(),TransactionType.CREDIT);



        accountOrigin.addTransaction(transactionOrigin);
        accountDestin.addTransaction(transactionDestin);

        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountDestin.setBalance(accountDestin.getBalance() + amount);

        transactionService.saveTransaction(transactionOrigin);
        transactionService.saveTransaction(transactionDestin);

        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountDestin);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}

