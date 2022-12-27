package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.utils.RandomNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private ClientLoanRepository clientLoanRepository;
	@Autowired
	private CardRepository cardRepository;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(){
		return (args) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("cliente"));
			Client client2 = new Client("Juan", "Sarmiento", "juancin@mindhub.com", passwordEncoder.encode("cliente"));
			Client client3 = new Client("Raul", "Alberto", "oso@mindhub.com", passwordEncoder.encode("cliente"));
			Client client4 = new Client("Pao", "PaiPai", "pao@mindhub.com", passwordEncoder.encode("cliente"));
			Client admin = new Client("dev", "dev", "admin@mindhub.com", passwordEncoder.encode("admin"));

			List<Client> clients = List.of(client1, client2, client3, client4, admin);
			clientRepository.saveAll(clients);

			Account account1 = new Account("VIN001", 5000, client1);
			Account account2 = new Account("VIN002", 7500, client1);
			Account account3 = new Account("VIN007", 5000, client2);
			Account account4 = new Account("VIN007", 7500, client2);

			List<Account> accounts = List.of(account1, account2, account3, account4);
			accountRepository.saveAll(accounts);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT, -1000, "Corte de Pelo", LocalDateTime.now(), account1);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 6000, "Lentes", LocalDateTime.now(), account1);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -2000, "Picada", LocalDateTime.now(), account1);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -3000, "Mouse", LocalDateTime.now(), account1);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 6000, "Teclado", LocalDateTime.now(), account1);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -2000, "Cervezas", LocalDateTime.now(), account1);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 600, "Youtube Premium", LocalDateTime.now(), account3);

			List<Transaction> transactions = List.of(transaction1, transaction2, transaction3, transaction4, transaction5, transaction6, transaction7);
			transactionRepository.saveAll(transactions);

			List<Integer> paymentsMortgageLoan = List.of(12, 24, 36, 48, 60);
			List<Integer> paymentsPersonalLoan = List.of(6, 12, 24);
			List<Integer> paymentsCarLoan = List.of(6, 12, 24, 36);

			Loan mortgageLoan = new Loan("Mortgage Loan", 500.000, paymentsMortgageLoan);
			Loan personalLoan = new Loan("Personal Loan", 100.000, paymentsPersonalLoan);
			Loan carLoan = new Loan("Car Loan", 300.000, paymentsCarLoan);

			List<Loan> loanList = List.of(mortgageLoan, personalLoan, carLoan);
			loanRepository.saveAll(loanList);

			ClientLoan clientLoan1 = new ClientLoan(400000D, 60, client1, mortgageLoan);
			ClientLoan clientLoan2 = new ClientLoan(50000D, 12, client1, personalLoan);
			ClientLoan clientLoan3 = new ClientLoan(100000D, 24, client2, personalLoan);
			ClientLoan clientLoan4 = new ClientLoan(200000D, 36, client2, carLoan);

			List<ClientLoan> clientLoanList = List.of(clientLoan1, clientLoan2, clientLoan3, clientLoan4);
			clientLoanRepository.saveAll(clientLoanList);

			Card card1 = new Card(client1, CardType.DEBIT, CardColor.GOLD, RandomNum.getRandonNumber4CardNum(), RandomNum.getRandomNum4CVV());
			Card card2 = new Card(client1, CardType.CREDIT, CardColor.TITANIUM, RandomNum.getRandonNumber4CardNum(), RandomNum.getRandomNum4CVV());
			Card card3 = new Card(client2, CardType.CREDIT, CardColor.SILVER, RandomNum.getRandonNumber4CardNum(), RandomNum.getRandomNum4CVV());

			List<Card> cards = List.of(card1, card2, card3);
			cardRepository.saveAll(cards);

		};
	}

}
