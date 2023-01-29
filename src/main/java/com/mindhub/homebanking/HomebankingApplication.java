package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.utils.AccountsUtils;
import com.mindhub.homebanking.utils.CardsUtils;
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

			Account account1 = new Account("VIN" + AccountsUtils.getNumber4VIN(), 5000, LocalDate.now(), client1, AccountType.CURRENT);
			Account account2 = new Account("VIN" + AccountsUtils.getNumber4VIN(), 7500, LocalDate.now(), client1, AccountType.SAVING);
			Account account3 = new Account("VIN" + AccountsUtils.getNumber4VIN(), 5000, LocalDate.now(), client2, AccountType.CURRENT);
			Account account4 = new Account("VIN" + AccountsUtils.getNumber4VIN(), 7500, LocalDate.now(), client2, AccountType.CURRENT);

			List<Account> accounts = List.of(account1, account2, account3, account4);
			accountRepository.saveAll(accounts);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT, 1000, "Corte de Pelo", LocalDateTime.now(), account1, account1.getBalance() - 1000);
			account1.setBalance(account1.getBalance() - 1000);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 6000, "Reintegro Lentes", LocalDateTime.now(), account1, account1.getBalance() + 6000);
			account1.setBalance(account1.getBalance() + 6000);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 2000, "Picada", LocalDateTime.now(), account1, account1.getBalance() - 2000);
			account1.setBalance(account1.getBalance() - 2000);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 3000, "Mouse", LocalDateTime.now(), account1, account1.getBalance() - 3000);
			account1.setBalance(account1.getBalance() - 3000);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 6000, "Reintegro Teclado", LocalDateTime.now(), account1, account1.getBalance() + 6000);
			account1.setBalance(account1.getBalance() + 6000);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, 2000, "Cervezas", LocalDateTime.now(), account1, account1.getBalance() - 2000);
			account1.setBalance(account1.getBalance() - 2000);
			Transaction transaction7 = new Transaction(TransactionType.CREDIT, 600, "Reintegro Youtube Premium", LocalDateTime.now(), account3, account3.getBalance() + 600);
			account3.setBalance(account3.getBalance() + 600);

			List<Transaction> transactions = List.of(transaction1, transaction2, transaction3, transaction4, transaction5, transaction6, transaction7);
			transactionRepository.saveAll(transactions);

			accountRepository.saveAll(List.of(account1, account3));

			List<Integer> paymentsMortgageLoan = List.of(12, 24, 36, 48, 60);
			List<Integer> paymentsPersonalLoan = List.of(6, 12, 24);
			List<Integer> paymentsCarLoan = List.of(6, 12, 24, 36);

			Loan personalLoan = new Loan("Personal Loan", 100000D, paymentsPersonalLoan, 40);
			Loan carLoan = new Loan("Car Loan", 300000D, paymentsCarLoan, 50);
			Loan mortgageLoan = new Loan("Mortgage Loan", 500000D, paymentsMortgageLoan, 65);

			List<Loan> loanList = List.of(mortgageLoan, personalLoan, carLoan);
			loanRepository.saveAll(loanList);

			ClientLoan clientLoan1 = new ClientLoan(400000D, 400000D * 1.65, 60, client1, mortgageLoan, LocalDate.now(), 400000 / 60);
			ClientLoan clientLoan2 = new ClientLoan(50000D, 50000D * 1.4, 12, client1, personalLoan, LocalDate.now(), 50000 / 12);
			ClientLoan clientLoan3 = new ClientLoan(100000D, 100000D * 1.4, 24, client2, personalLoan, LocalDate.now(), 100000 / 24);
			ClientLoan clientLoan4 = new ClientLoan(200000D, 200000D * 1.5, 36, client2, carLoan, LocalDate.now(), 200000 / 36);

			List<ClientLoan> clientLoanList = List.of(clientLoan1, clientLoan2, clientLoan3, clientLoan4);
			clientLoanRepository.saveAll(clientLoanList);

			Card card1 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.DEBIT, CardColor.GOLD, CardsUtils.getCardNumber(), CardsUtils.getCVV(), LocalDate.now(), LocalDate.now().plusYears(5), client1, account1);
			Card card2 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM, CardsUtils.getCardNumber(), CardsUtils.getCVV(), LocalDate.now(), LocalDate.now().plusYears(5), client1, account2);
			Card card3 = new Card(client2.getFirstName() + " " + client2.getLastName(), CardType.CREDIT, CardColor.SILVER, CardsUtils.getCardNumber(), CardsUtils.getCVV(), LocalDate.now(), LocalDate.now().plusYears(5), client2, account3);

			List<Card> cards = List.of(card1, card2, card3);
			cardRepository.saveAll(cards);

		};
	}

}
