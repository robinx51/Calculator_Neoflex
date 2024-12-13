package Neoflex_bank.MS_deal.service;

import Neoflex_bank.MS_calculator.dto.LoanOfferDto;
import Neoflex_bank.MS_calculator.dto.LoanStatementRequestDto;
import Neoflex_bank.MS_calculator.services.CalcService;
import Neoflex_bank.MS_deal.db_pgsql.DB_neoflex_bank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.sql.*;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class StatementService {
    private final DB_neoflex_bank dbNeoflexBank;

    public void processClient(LoanStatementRequestDto request) {
        dbNeoflexBank.insertUser(request);
    }

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto request) {
        return RestClient.create().post()
                .uri("http://localhost:8080/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
