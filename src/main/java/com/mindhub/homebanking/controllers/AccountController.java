package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class AccountController {




    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccountsDTO() {

        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountDTO(@PathVariable Long id)
    {
        return accountService.getAccountDTO(id);
    }



    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    @PostMapping("/clients/current/accounts")

    public ResponseEntity<Object> createAccount(Authentication authentication,@RequestParam AccountType type) {



        Client clientCurrent = clientService.findByEmail(authentication.getName());

        if(type.equals(null))
        {
            return new ResponseEntity<>("Account Type is Empty", HttpStatus.BAD_REQUEST);
        }


        if(clientCurrent.getAccounts().stream().filter(account -> account.getActive().equals(true)).collect(Collectors.toSet()).size() > 3)
        {
            return new ResponseEntity<>("Max amount of Accounts", HttpStatus.FORBIDDEN);
        }
        else{

            Account accountNew = new Account("VIN" + getRandomNumber(100000,10000000), 0.0, LocalDateTime.now(), type , true);
            clientCurrent.addAccount(accountNew);
            accountService.saveAccount(accountNew);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }



    }

    @PatchMapping("/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(@RequestParam String number){
        Account deletedAccount = accountService.findByNumber(number);
        if (deletedAccount.getBalance() > 0) {
            return new ResponseEntity<>("Account balance is bigger than $0", HttpStatus.FORBIDDEN);
        }
        deletedAccount.setActive(false);
        accountService.saveAccount(deletedAccount);
        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }


}
