package com.mindhub.homebanking.services.Impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.services.PDFGeneratorService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorService {

    @Autowired
    private TransactionService transactionService;

    @Override
    public void export(HttpServletResponse response, String accountNumber, LocalDateTime fromDate, LocalDateTime toDate) throws IOException {


        List<TransactionDTO> transactions = transactionService.findByDateBetween(fromDate, toDate)
                            .stream().filter(transaction -> transaction.getAccount()
                            .getNumber()
                            .equalsIgnoreCase(accountNumber))
                            .map(transaction -> transactionService.transactionToTransactionDTO(transaction))
                            .toList();

        //List<TransactionDTO> transactions = transactionService.transactionsToTransactionsDTO(transactionService.findByDateBetween(fromDate, toDate));

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("Transactions of Account: " + accountNumber, fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph.setSpacingAfter(10);
        document.add(paragraph);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        if(transactions.size() == 1){
            Paragraph paragraph2 = new Paragraph();
            paragraph2.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            paragraph2.setSpacingAfter(5);
            paragraph2.add(transactions.toString());
            document.add(paragraph2);
        }else{
            transactions.forEach(transaction -> {
                Paragraph paragraph2 = new Paragraph();
                paragraph2.setAlignment(Paragraph.ALIGN_JUSTIFIED);
                paragraph2.setSpacingAfter(5);
                paragraph2.add(transaction.toString());
                document.add(paragraph2);
            });
        }

        document.close();
    }
}
