package Neoflex_bank.MS_deal.service;

import Neoflex_bank.MS_calculator.dto.LoanOfferDto;
import Neoflex_bank.MS_calculator.dto.LoanStatementRequestDto;
import Neoflex_bank.MS_deal.db_pgsql.entity.Client;
import Neoflex_bank.MS_deal.db_pgsql.entity.Statement;
import Neoflex_bank.MS_deal.db_pgsql.service.ClientServiceDB;
import Neoflex_bank.MS_deal.db_pgsql.service.StatementServiceDB;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementService {
    private final ClientServiceDB clientService;
    private final StatementServiceDB statementServiceDB;

    public List<LoanOfferDto> processClient(LoanStatementRequestDto request) {
        Client client = clientService.createClient(request);
        Statement statement = statementServiceDB.createStatement(client.getClient_id());
        return setClientIds(getOffers(request), statement.getStatement_id());
    }

    private List<LoanOfferDto> getOffers(LoanStatementRequestDto request) {
        return RestClient.create().post()
                .uri("http://localhost:8080/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    private List<LoanOfferDto> setClientIds(List<LoanOfferDto> offers, UUID statement_id) {
        for(LoanOfferDto offer : offers) {
            offer.setStatementId(statement_id);
        }
        return offers;
    }
}
