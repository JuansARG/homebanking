package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

    @Autowired
    LoanService loanService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.loansToLoansDTO(loanService.getAllLoans());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> requestLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication auth){

        Client currentClient = clientService.getClientByEmail(auth.getName());
        Loan currentLoan = loanService.getLoanById(loanApplicationDTO.getIdOfLoan());
        Account destinationAccount = accountService.getAccountByNumber(loanApplicationDTO.getDestinationAccountNumber());

        if(loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("Invalid amount.", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("Invalid quotas.", HttpStatus.FORBIDDEN);
        }

        if(currentLoan == null){
            return new ResponseEntity<>("The type of loan requested does not exist.", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() > currentLoan.getMaxAmount()){
            return new ResponseEntity<>("The amount exceeds the requested loan limit.", HttpStatus.FORBIDDEN);
        }

        if(!currentLoan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("The requested installments do not correspond to the type of loan requested.", HttpStatus.FORBIDDEN);
        }

        if(destinationAccount == null){
            return new ResponseEntity<>("The destination account does not exist.", HttpStatus.FORBIDDEN);
        }

        if(currentClient.getAccounts().stream().noneMatch(account -> account.getId().equals(destinationAccount.getId()))){
            return new ResponseEntity<>("The destination account does not belong to the authenticated client.", HttpStatus.FORBIDDEN);
        }

        //VERIFICAR SI EL CURRENT CLIENT YA POSEE UN PRESTAMO DEL TIPO SOLICITADO
        List<ClientLoan> listClientLoan = currentClient.getLoans().stream().toList();
        if(listClientLoan.stream().filter(ClientLoan::isEnable).anyMatch(clientLoan -> clientLoan.getLoan().equals(currentLoan))){
            return new ResponseEntity<>("You already have a loan with the type requested.", HttpStatus.FORBIDDEN);
        }

        double finalAmount = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * (currentLoan.getInterestRate()) / 100);

        Transaction transaction = transactionService.createTransaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), currentLoan.getName() + " Loan Approved", LocalDateTime.now(), destinationAccount, destinationAccount.getBalance() + loanApplicationDTO.getAmount());
        transactionService.saveTransaction(transaction);

        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplicationDTO.getAmount());
        accountService.saveAccount(destinationAccount);


        ClientLoan clientLoan = clientLoanService.createClientLoan(
                                                                    loanApplicationDTO.getAmount(),
                                                                    finalAmount,
                                                                    loanApplicationDTO.getPayments(),
                                                                    currentClient,
                                                                    currentLoan,
                                                                    LocalDate.now(),
                                                                    finalAmount / loanApplicationDTO.getPayments());
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>("Everything has gone well!", HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/loans")
    public ResponseEntity<Object> pay(@RequestParam Long idCurrentClientLoan,
                                      @RequestParam Integer dues,
                                      @RequestParam Double value,
                                      @RequestParam String accountNumber,
                                      Authentication auth){

        Client currentClient = clientService.getClientByEmail(auth.getName());
        Account currentAccount =  accountService.getAccountByNumber(accountNumber);
        ClientLoan currentClientLoan =  clientLoanService.getClientLoanById(idCurrentClientLoan);
        Loan typeOfLoan = currentClientLoan.getLoan();
        Double amount = dues * value;

        //VERIFICAR QUE EL NUMERO DE CUENTA NO ESTE VACIO
        if(accountNumber.isEmpty()){
            return new ResponseEntity<>("You must select the account with which you are going to pay.", HttpStatus.FORBIDDEN);
        }

        //VERIFICAR QUE EL NUMERO DE CUOTAS NO SEA MENOR O IGUAL A 0
        if(dues <= 0){
            return new ResponseEntity<>("The number of installments to pay must be greater than 0.", HttpStatus.FORBIDDEN);
        }

        //VERIFICAR QUE EL CLIENTE ESTE AUTHENTICADO
        if(currentClient == null){
            return new ResponseEntity<>("Whoever wants to do the operation is not authenticated.", HttpStatus.FORBIDDEN);
        }

        //VERIFICAR QUE LA CUENTA CON LA QUE QUIERA PAGAR LE PETERNEZCA AL CLIENTE AUTENTICADO
        if(currentClient.getAccounts().stream().noneMatch(account -> account.getId().equals(currentAccount.getId()))){
            return new ResponseEntity<>("The account you want to pay with does not belong to the authenticated customer.", HttpStatus.FORBIDDEN);
        }

        //VERIFICAR QUE EL PRESTAMO A PAGAR PERTENEZCA AL CLIENTE AUTENTICADO
        if(currentClient.getLoans().stream().noneMatch(clientLoan -> clientLoan.getId().equals(currentClientLoan.getId()))){
            return new ResponseEntity<>("The loan on which you want to pay installments does not belong to the authenticated customer.", HttpStatus.FORBIDDEN);
        }

        //VERIFICAR QUE LAS CUOTAS QUE VA A PAGAR NO SEA MAYOR QUE LAS CUOTAS PENDIENTES
        if(dues > currentClientLoan.getRemainingPayments()){
            return new ResponseEntity<>("You cannot pay more installments than you have left.", HttpStatus.FORBIDDEN);
        }

        //VERIFICAR QUE EL PRESTAMO SOBRE EL QUE QUIERE PAGAR, EXISTA
        if(currentClientLoan == null){
            return new ResponseEntity<>("The loan for which you wish to pay installments does not exist." ,HttpStatus.FORBIDDEN);
        }

        //VERIFICAR QUE EL MONTO A PAGAR NO SEA MAYOR AL MONTO RESTANTE
        if(value > currentClientLoan.getFinalAmount()){
            return new ResponseEntity<>("The amount to be paid is greater than the total amount remaining.", HttpStatus.FORBIDDEN);
        }

        //VERIFICAR QUE EL BALANACE DE LA CUENTA NO SEA MENOR AL VALOR DE LAS CUOTAS A PAGAR
        if(currentAccount.getBalance() < amount){
            return new ResponseEntity<>("The account you want to pay with has insufficient funds.", HttpStatus.FORBIDDEN);
        }

        String transactionDescription = "Advance of " + dues + " installments of " + typeOfLoan.getName();

        Transaction transaction = transactionService.createTransaction(TransactionType.DEBIT, amount, transactionDescription, LocalDateTime.now(), currentAccount, currentAccount.getBalance() - amount);
        transactionService.saveTransaction(transaction);

        currentAccount.setBalance(currentAccount.getBalance() - amount);
        accountService.saveAccount(currentAccount);

        if(currentClientLoan.getRemainingPayments() - dues == 0){
            currentClientLoan.setRemainingPayments(currentClientLoan.getRemainingPayments() - dues);
            currentClientLoan.setRestAmount(currentClientLoan.getRestAmount() - amount);
            clientLoanService.deleteClientLoanById(idCurrentClientLoan);
        }else {
            currentClientLoan.setRemainingPayments(currentClientLoan.getRemainingPayments() - dues);
            currentClientLoan.setRestAmount(currentClientLoan.getRestAmount() - amount);
            clientLoanService.saveClientLoan(currentClientLoan);
        }

        return new ResponseEntity<>("Everything has gone well.", HttpStatus.OK);
    }

    @PostMapping("/create/loan")
    public ResponseEntity<Object> createLoanByAdmin(@RequestParam double maxAmount,
                                                    @RequestParam List<Integer> payments,
                                                    @RequestParam String nameLoanBased,
                                                    Authentication auth){
        Loan currentLoan = loanService.getLoanByName(nameLoanBased);
        List<String> authorities = auth.getAuthorities().stream().map(Object::toString).toList();

        if(!authorities.contains("ADMIN")){
            return new ResponseEntity<>("You do not possess the necessary authority.", HttpStatus.FORBIDDEN);
        }

        Loan newLoan = new Loan(currentLoan.getName(), maxAmount, payments, currentLoan.getInterestRate());
        loanService.saveLoan(newLoan);
        return new ResponseEntity<>("New Loan Created: " + newLoan.toString(), HttpStatus.CREATED);
    }
}
