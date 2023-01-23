package com.mindhub.homebanking.services;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public interface PDFGeneratorService {

    void export(HttpServletResponse response, String accountNumber, LocalDateTime fromDate, LocalDateTime toDate) throws IOException;
}
