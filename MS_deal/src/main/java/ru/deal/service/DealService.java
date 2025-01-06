package ru.deal.service;

import ru.deal.FeignClient.CalculatorFeignClient;
import ru.deal.db_pgsql.entity.*;
import ru.deal.db_pgsql.service.*;
import ru.deal.dto.FinishRegistrationRequestDto;
import ru.deal.dto.PassportDto;
import ru.deal.dto.StatementStatusHistoryDto;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.calculator.dto.CreditDto;
import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;
import ru.calculator.dto.ScoringDataDto;

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
        Statement statement = statementServiceDB.createStatement(client);
        addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.STATEMENT_CREATED);
        logger.info("Заявка на расчёт обработана");
        return setStatementIds(calculatorFeignClient.getOffers(request), statement.getStatementId());
    }

    public void selectOffer(LoanOfferDto request) {
        logger.info("Получен запрос на выбор кредитного предложения");
        Credit credit = creditServiceDB.createCredit(request.getIsInsuranceEnabled(), request.getIsSalaryClient());
        Statement statement = statementServiceDB.getStatementById(request.getStatementId());
        statement.setAppliedOffer(request);
        statement.setCredit(credit);
        addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.PREPARE_DOCUMENTS);
        logger.info("Запрос на выбор предложения обработан");
    }

    public void finishRegistration(String statementId, FinishRegistrationRequestDto request) {
        logger.info("Получен запрос на завершение регистрации и полный подсчёт кредита");
        Statement statement = statementServiceDB.getStatementById(UUID.fromString(statementId));
        Client client = clientService.getClientById(statement.getClient().getClientId());

        setClientByFinishRegistrationRequestDto(client, request);
        clientService.updateClient(client);

        ScoringDataDto scoringDataDto = setScoringDataDto(statement, client);
        CreditDto creditDto = calculatorFeignClient.getCreditDto(scoringDataDto);
        Credit credit = statement.getCredit();
        setCreditByCreditDto(credit, creditDto);
        creditServiceDB.updateCredit(credit);
        addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.DOCUMENT_CREATED);
        logger.info("Запрос на завершение регистрации и полный подсчёт кредита обработан");
    }

    public List<Statement> getStatements() {
        return statementServiceDB.statementRepository.findAll();
    }

    private List<LoanOfferDto> setStatementIds(List<LoanOfferDto> offers, UUID statement_id) {
        for(LoanOfferDto offer : offers) {
            offer.setStatementId(statement_id);
        }
        return offers;
    }

    private void addStatementStatusAndUpdate(Statement statement, Statement.eApplicationStatus status) {
        List<StatementStatusHistoryDto> list;
        if (statement.getStatusHistory() == null) {
            list = new ArrayList<>();
            statement.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
            statement.setStatus(status);
        }
        else
            list = statement.getStatusHistory();

        StatementStatusHistoryDto statusDto = StatementStatusHistoryDto.builder()
                .status(status)
                .time(LocalDateTime.now())
                .changeType(StatementStatusHistoryDto.eChangeType.MANUAL)
                .build();
        list.add(statusDto);
        statement.setStatus(status);
        statement.setStatusHistory(list);
        statementServiceDB.updateStatement(statement);
    }

    private ScoringDataDto setScoringDataDto(Statement statement, Client client) {
        Credit credit = statement.getCredit();
        return ScoringDataDto.builder()
                .amount(statement.getAppliedOffer().getRequestedAmount())
                .term(statement.getAppliedOffer().getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(client.getGender())
                .birthdate(client.getBirth_date().toLocalDate())
                .passportSeries(client.getPassportDto().getPassportSeries())
                .passportNumber(client.getPassportDto().getPassportNumber())
                .passportIssueDate(client.getPassportDto().getPassportIssueDate())
                .passportIssueBranch(client.getPassportDto().getPassportIssueBranch())
                .maritalStatus(client.getMartialStatus())
                .dependentAmount(client.getDependentAmount())
                .employment(client.getEmployment())
                .accountNumber(client.getAccountNumber())
                .isInsuranceEnabled(credit.getInsuranceEnabled())
                .isSalaryClient(credit.getSalaryClient())
                .build();
    }

    private void setCreditByCreditDto(Credit credit, CreditDto creditDto) {
        credit.setAmount(creditDto.getAmount());
        credit.setTerm(creditDto.getTerm());
        credit.setMonthlyPayment(creditDto.getMonthlyPayment());
        credit.setRate(creditDto.getRate());
        credit.setPsk(creditDto.getPsk());
        credit.setPaymentSchedule(creditDto.getPaymentSchedule());
        credit.setCreditStatus(Credit.eCreditStatus.CALCULATED);
    }

    private void setClientByFinishRegistrationRequestDto(Client client, FinishRegistrationRequestDto request) {
        PassportDto passportDto = client.getPassportDto();
        passportDto.setPassportIssueDate(request.getPassportIssueDate());
        passportDto.setPassportIssueBranch(request.getPassportIssueBranch());

        client.setGender(request.getGender());
        client.setMartialStatus(request.getMaritalStatus());
        client.setDependentAmount(request.getDependentAmount());
        client.setPassportDto(passportDto);
        client.setEmployment(request.getEmployment());
        client.setAccountNumber(request.getAccountNumber());
    }
}