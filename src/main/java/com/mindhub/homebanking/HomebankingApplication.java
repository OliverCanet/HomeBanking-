package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);




	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository , AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository ,PasswordEncoder passwordEncoder ){
		return args -> {
		Client client1 = new Client("Melba","Morel","melba@mindhub.com", passwordEncoder.encode("melba"));
		Client client2 = new Client("Oliver","Canet","olivercanet@hotmail.com", passwordEncoder.encode("elmascapo"));
		Client client3 = new Client("Jose","Sanchez","josesanchez2020@hotmail.com", passwordEncoder.encode("123"));
		Client client4 = new Client("Manuel","Belgrano","manubelgrano@hotmail.com", passwordEncoder.encode("messicr7"));
		Client client5 = new Client("admin","admin","admin@mail.com", passwordEncoder.encode("admin"));


		clientRepository.save(client1);
		clientRepository.save(client2);
		clientRepository.save(client3);
		clientRepository.save(client4);
		clientRepository.save(client5);

		Account account1 = new Account("VIN001",12.3, LocalDateTime.now(),AccountType.CHECKING,true);
		client1.addAccount(account1);
		accountRepository.save(account1);
		Account account2 = new Account("VIN002",61920.1, LocalDateTime.now(),AccountType.SAVINGS,true);
		client1.addAccount(account2);
		accountRepository.save(account2);
		Account account5 = new Account("VIN005",1000022.3, LocalDateTime.now(),AccountType.CHECKING,true);
		client2.addAccount(account5);
		accountRepository.save(account5);


		Transaction transaction1 = new Transaction(-10,"BLT Sandwich",LocalDateTime.now(), TransactionType.DEBIT);
		account1.addTransaction(transaction1);
		transactionRepository.save(transaction1);
		Transaction transaction3 = new Transaction(122,"Fifa 22",LocalDateTime.now(), TransactionType.CREDIT);
		account1.addTransaction(transaction3);
		transactionRepository.save(transaction3);

		Transaction transaction2 = new Transaction(110000,"Fiat 500",LocalDateTime.now(), TransactionType.CREDIT);
		account5.addTransaction(transaction2);
		transactionRepository.save(transaction2);

		Loan loan1 = new Loan("Mortgage",500000, List.of(12,24,36,48,60));
		Loan loan2 = new Loan("Personal",100000, List.of( 6,12,24));
		Loan loan3 = new Loan("Automotive",300000, List.of(6,12,24,36));

		loanRepository.save(loan1);
		loanRepository.save(loan2);
		loanRepository.save(loan3);




		Card card1 = new Card("Melba Morel",CardType.DEBIT,CardColor.GOLD,"4545-6543-3883-9999",450, LocalDate.now().plusYears(5),LocalDate.now(),true);
		client1.addCard(card1);
		cardRepository.save(card1);
		Card card2 = new Card("Melba Morel",CardType.DEBIT,CardColor.TITANIUM,"4540-6678-3245-2334",871, LocalDate.now().plusYears(5),LocalDate.now(),true);
		client1.addCard(card2);
		cardRepository.save(card2);
		Card card3 = new Card("Melba Morel",CardType.CREDIT,CardColor.GOLD,"4545-6543-3883-1234",223, LocalDate.now().plusYears(5),LocalDate.now(),true);
		client1.addCard(card3);
		cardRepository.save(card3);



		};
	}


}
