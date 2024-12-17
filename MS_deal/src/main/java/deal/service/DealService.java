package deal.service;

import deal.FeignClient.CalculatorFeignClient;
import deal.db_pgsql.entity.*;
import deal.db_pgsql.service.*;
import deal.dto.FinishRegistrationRequestDto;
import deal.dto.PassportDto;
import deal.dto.StatementStatusHistoryDto;
import calculator.dto.*;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealService {
    private final ClientServiceDB clientService;
    private final StatementServiceDB statementServiceDB;
    private final CreditServiceDB creditServiceDB;
    private final CalculatorFeignClient calculatorFeignClient;
    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    public List<LoanOfferDto> processClient(LoanStatementRequestDto request) {
        logger.info("Получена заявка на расчёт возможных условий кредита");
        Client client = clientService.createClient(request);
        Statement statement = statementServiceDB.createStatement(client.getClient_id());
        addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.STATEMENT_CREATED);
        logger.info("Заявка на расчёт обработана");
        return setStatementIds(calculatorFeignClient.getOffers(request), statement.getStatement_id());
    }

    public void selectOffer(LoanOfferDto request) {
        logger.info("Получен запрос на выбор кредитного предложения");
        Credit credit = creditServiceDB.createCredit(request.getIsInsuranceEnabled(), request.getIsSalaryClient());
        Statement statement = statementServiceDB.getStatementById(request.getStatementId());
        statement.setApplied_offer(request);
        statement.setCredit_id(credit.getCredit_id());
        addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.PREPARE_DOCUMENTS);
        logger.info("Запрос на выбор предложения обработан");
    }

    public void finishRegistration(String statementId, FinishRegistrationRequestDto request) {
        logger.info("Получен запрос на завершение регистрации и полный подсчёт кредита");
        Statement statement = statementServiceDB.getStatementById(UUID.fromString(statementId));
        Client client = clientService.getClientById(statement.getClient_id());

        setClientByFinishRegistrationRequestDto(client, request);
        clientService.updateClient(client);

        ScoringDataDto scoringDataDto = setScoringDataDto(statement, client);
        CreditDto creditDto = getCreditDto(scoringDataDto);
        Credit credit = creditServiceDB.getCreditById(statement.getCredit_id());
        setCreditByCreditDto(credit, creditDto);
        creditServiceDB.updateCredit(credit);
        addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.DOCUMENT_CREATED);
        logger.info("Запрос на завершение регистрации и полный подсчёт кредита обработан");
    }

    private CreditDto getCreditDto(ScoringDataDto request) {
        return RestClient.create().post()
                .uri("http://localhost:8080/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    private List<LoanOfferDto> setStatementIds(List<LoanOfferDto> offers, UUID statement_id) {
        for(LoanOfferDto offer : offers) {
            offer.setStatementId(statement_id);
        }
        return offers;
    }

    private void addStatementStatusAndUpdate(Statement statement, Statement.eApplicationStatus status) {
        List<StatementStatusHistoryDto> list;
        if (statement.getStatus_history() == null) {
            list = new ArrayList<>();
            statement.setCreation_date(Timestamp.valueOf(LocalDateTime.now()));
            statement.setStatus(status);
        }
        else
            list = statement.getStatus_history();

        StatementStatusHistoryDto statusDto = StatementStatusHistoryDto.builder()
                .status(status)
                .time(LocalDateTime.now())
                .changeType(StatementStatusHistoryDto.eChangeType.MANUAL)
                .build();
        list.add(statusDto);
        statement.setStatus(status);
        statement.setStatus_history(list);
        statementServiceDB.updateStatement(statement);
    }

    private ScoringDataDto setScoringDataDto(Statement statement, Client client) {
        Credit credit = creditServiceDB.getCreditById(statement.getCredit_id());
        return ScoringDataDto.builder()
                .amount(statement.getApplied_offer().getRequestedAmount())
                .term(statement.getApplied_offer().getTerm())
                .firstName(client.getFirst_name())
                .lastName(client.getLast_name())
                .middleName(client.getMiddle_name())
                .gender(client.getGender())
                .birthdate(client.getBirth_date().toLocalDate())
                .passportSeries(client.getPassportDto().getPassportSeries())
                .passportNumber(client.getPassportDto().getPassportNumber())
                .passportIssueDate(client.getPassportDto().getPassportIssueDate())
                .passportIssueBranch(client.getPassportDto().getPassportIssueBranch())
                .maritalStatus(client.getMartial_status())
                .dependentAmount(client.getDependent_amount())
                .employment(client.getEmployment())
                .accountNumber(client.getAccount_number())
                .isInsuranceEnabled(credit.getInsurance_enabled())
                .isSalaryClient(credit.getSalary_client())
                .build();
    }

    private void setCreditByCreditDto(Credit credit, CreditDto creditDto) {
        credit.setAmount(creditDto.getAmount());
        credit.setTerm(creditDto.getTerm());
        credit.setMonthly_payment(creditDto.getMonthlyPayment());
        credit.setRate(creditDto.getRate());
        credit.setPsk(creditDto.getPsk());
        credit.setPayment_schedule(creditDto.getPaymentSchedule());
        credit.setCredit_status(Credit.eCreditStatus.CALCULATED);
    }

    private void setClientByFinishRegistrationRequestDto(Client client, FinishRegistrationRequestDto request) {
        PassportDto passportDto = client.getPassportDto();
        passportDto.setPassportIssueDate(request.getPassportIssueDate());
        passportDto.setPassportIssueBranch(request.getPassportIssueBranch());

        client.setGender(request.getGender());
        client.setMartial_status(request.getMaritalStatus());
        client.setDependent_amount(request.getDependentAmount());
        client.setPassportDto(passportDto);
        client.setEmployment(request.getEmployment());
        client.setAccount_number(request.getAccountNumber());
    }
}