package Neoflex_bank.MS_deal.service;

import Neoflex_bank.MS_calculator.dto.LoanOfferDto;
import Neoflex_bank.MS_calculator.dto.LoanStatementRequestDto;
import Neoflex_bank.MS_deal.db_pgsql.entity.Client;
import Neoflex_bank.MS_deal.db_pgsql.entity.Statement;
import Neoflex_bank.MS_deal.db_pgsql.service.ClientServiceDB;
import Neoflex_bank.MS_deal.db_pgsql.service.StatementServiceDB;
import Neoflex_bank.MS_deal.dto.StatementStatusHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealService {
    private final ClientServiceDB clientService;
    private final StatementServiceDB statementServiceDB;

    public List<LoanOfferDto> processClient(LoanStatementRequestDto request) {
        Client client = clientService.createClient(request);
        Statement statement = statementServiceDB.createStatement(client.getClient_id());
        return setClientIds(getOffers(request), statement.getStatement_id());
    }

    public void selectOffer(LoanOfferDto request) {
        Statement statement = statementServiceDB.getStatementById(request.getStatementId());
        statement.setApplied_offer(request);
        statement.setStatus_history(addStatus(statement, Statement.eApplicationStatus.PREPARE_DOCUMENTS));
        statementServiceDB.updateStatement(statement);
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

    private List<StatementStatusHistoryDto> addStatus(Statement statement, Statement.eApplicationStatus status) {
        List<StatementStatusHistoryDto> list;
        if (statement.getStatus_history() == null)
            list = new ArrayList<>();
        else
            list = statement.getStatus_history();

        StatementStatusHistoryDto statusDto = StatementStatusHistoryDto.builder()
                .status(status)
                .time(LocalDateTime.now())
                .changeType(StatementStatusHistoryDto.eChangeType.MANUAL)
                .build();
        list.add(statusDto);
        return list;
    }
}
