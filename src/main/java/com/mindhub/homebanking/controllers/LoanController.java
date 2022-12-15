package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreateDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {



    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoansDTO() {

        return loanService.getLoansDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> applyLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {



        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Loan loanExist = loanService.findById(loanApplicationDTO.getIdLoan());
        Account accountD = accountService.findByNumber(loanApplicationDTO.getAccountDestiny());






            if(clientCurrent == null){
                return new ResponseEntity<>("Wrong Client", HttpStatus.BAD_REQUEST);


            }
            if(loanApplicationDTO == null)
            {
                return new ResponseEntity<>("Missing Data", HttpStatus.BAD_REQUEST);
            }
            if (loanApplicationDTO.getAmount() == 0)
            {
                return new ResponseEntity<>("Missing Amount", HttpStatus.BAD_REQUEST);
            }
            if (loanApplicationDTO.getPayments().isEmpty())
            {
                return new ResponseEntity<>("Missing Payments", HttpStatus.BAD_REQUEST);
            }
            if(loanExist == null)
            {
                return new ResponseEntity<>("Loan Doesen't Exist", HttpStatus.BAD_REQUEST);
            }
            if(loanApplicationDTO.getAmount() > loanExist.getMaxAmount())
            {
                return new ResponseEntity<>("Amount Greater than Max Amount", HttpStatus.BAD_REQUEST);
            }
            if(accountD == null)
            {
                return new ResponseEntity<>("Destiny Account Doesent Exist", HttpStatus.BAD_REQUEST);
            }
            if(!clientCurrent.getAccounts().contains(accountD))
            {
                return new ResponseEntity<>("Client does not Contain Account Selected", HttpStatus.BAD_REQUEST);
            }
            if(clientCurrent.getClientLoans().size() >= 3)
            {
                return new ResponseEntity<>("Client does not Contain Account Selected", HttpStatus.FORBIDDEN);
            }
            if(clientCurrent.getClientLoans().stream().filter(clientLoan -> clientLoan.getLoan().getName().equals(loanExist.getName())).toArray().length >= 1)
            {
                return new ResponseEntity<>("Client already has loan", HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getIdLoan() == 12)
            {
                ClientLoan loan = new ClientLoan(loanApplicationDTO.getAmount() * 1.05, loanApplicationDTO.getPayments(),loanExist,clientCurrent);
                clientLoanService.saveClientLoan(loan);
            }
            if(loanApplicationDTO.getIdLoan() == 13)
            {
                ClientLoan loan = new ClientLoan(loanApplicationDTO.getAmount() * 1.10, loanApplicationDTO.getPayments(),loanExist,clientCurrent);
                clientLoanService.saveClientLoan(loan);
            }
            if(loanApplicationDTO.getIdLoan() == 14)
            {
                ClientLoan loan = new ClientLoan(loanApplicationDTO.getAmount() * 1.5, loanApplicationDTO.getPayments(),loanExist,clientCurrent);
                clientLoanService.saveClientLoan(loan);
            }




            Transaction transactionLoan = new Transaction(loanApplicationDTO.getAmount(), loanExist.getName() + "Loan Approved" + accountD.getNumber(), LocalDateTime.now(),TransactionType.CREDIT);

            accountD.addTransaction(transactionLoan);

            accountD.setBalance(accountD.getBalance() + loanApplicationDTO.getAmount());

            transactionService.saveTransaction(transactionLoan);

            accountService.saveAccount(accountD);
            return new ResponseEntity<>(HttpStatus.CREATED);







    }

    @Transactional
    @PostMapping("/loans/create")
    public ResponseEntity<Object> createLoan(@RequestBody LoanCreateDTO loanCreateDTO)
    {



        if(loanCreateDTO.getName().isEmpty()){
            return new ResponseEntity<>("Loan name is empty", HttpStatus.BAD_REQUEST);
        }
        if(loanCreateDTO.getMaxAmount() == 0)
        {
            return new ResponseEntity<>("Loan Max Amount is empty", HttpStatus.BAD_REQUEST);
        }
        if (loanCreateDTO.getPayments().isEmpty())
        {
            return new ResponseEntity<>("Missing Payments", HttpStatus.BAD_REQUEST);
        }

        Loan loan = new Loan(loanCreateDTO.getName(),loanCreateDTO.getMaxAmount(),loanCreateDTO.getPayments() );
        loanService.saveLoan(loan);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }




}
