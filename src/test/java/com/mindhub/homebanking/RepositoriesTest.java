package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {





    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @MockBean
    PasswordEncoder passwordEncoder;



        @Test
        public void existLoans(){

            List<Loan> loans = loanRepository.findAll();

            assertThat(loans,is(not(empty())));


        }



        @Test
        public void existPersonalLoan(){

            List<Loan> loans = loanRepository.findAll();

            assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

        }

        @Test
        public void existAccount(){
            List<Account> accounts = accountRepository.findAll();

            assertThat(accounts,is(not(empty())));

        }

        @Test
        public void existSpecificAccount(){

            List<Account> accounts = accountRepository.findAll();

            assertThat(accounts,hasItem(hasProperty("number",is("VIN001"))));

        }

        @Test
        public void existCard(){
            List<Card> cards = cardRepository.findAll();

            assertThat(cards,is(not(empty())));

        }

        /*@Test
        public void existGoldCard(){

            List<Card> cards = cardRepository.findAll();

            assertThat(cards,hasItem(hasProperty("color",is("Color.GOLD"))));

        }*/

        @Test
        public void existFiat500Transaction(){
            List<Transaction> transactions = transactionRepository.findAll();

            assertThat(transactions,hasItem(hasProperty("description",is("Fiat 500"))));
        }






}
