package ru.deal.service;

import jakarta.validation.ValidationException;
import ru.calculator.dto.*;
import ru.deal.FeignClient.CalculatorFeignClient;
import ru.deal.db_pgsql.entity.*;
import ru.deal.db_pgsql.service.*;
import ru.deal.dto.EmailMessageDto;
import ru.deal.dto.FinishRegistrationRequestDto;
import ru.deal.dto.PassportDto;
import ru.deal.dto.StatementStatusHistoryDto;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.deal.exception.FeignValidationException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealService {
    private final ClientServiceDB clientService;
    private final StatementServiceDB statementServiceDB;
    private final CreditServiceDB creditServiceDB;
    private final CalculatorFeignClient calculatorFeignClient;
    private final KafkaService kafkaService;
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

        EmailMessageDto emailMessageDto = createEmailMessageDto(
                statement.getStatementId().toString(),
                EmailMessageDto.Theme.finishRegistration,
                "Ваша заявка предварительно одобрена, завершите оформление");

        kafkaService.sendFinishRegistrationEmail(emailMessageDto);

        logger.info("Запрос на выбор предложения обработан");
    }

    public void finishRegistration(String statementId, FinishRegistrationRequestDto request) throws FeignValidationException {
        logger.info("Получен запрос на завершение регистрации и полный подсчёт кредита");
        Statement statement = statementServiceDB.getStatementById(UUID.fromString(statementId));
        Client client = clientService.getClientById(statement.getClient().getClientId());

        setClientByFinishRegistrationRequestDto(client, request);
        clientService.updateClient(client);

        processCalculationRequest(statement, statementId, setScoringDataDto(statement, client));
    }


    public void sendDocuments(String statementId) {
        EmailMessageDto emailMessageDto = createEmailMessageDto(
                statementId,
                EmailMessageDto.Theme.sendDocuments,
                "Документы отправлены");
        kafkaService.sendSendDocumentsEmail(emailMessageDto);
    }

    public void signRequestDocuments(String statementId) {
        Random rnd = new Random();
        int sesCode = 100000 + rnd.nextInt(900000);
        String messageText =
                "Документы по вашей заявке подписаны.\n" +
                "Id заявки: " + statementId + "\n" +
                "Код подтверждения заявки: " + sesCode;

        EmailMessageDto emailMessageDto = createEmailMessageDto(
                statementId,
                EmailMessageDto.Theme.sendSes,
                messageText);
        kafkaService.sendSendSesEmail(emailMessageDto);

        Statement statement = statementServiceDB.getStatementById(UUID.fromString(statementId));
        statement.setSesCode(sesCode);
        statement.setSignDate(Timestamp.valueOf(LocalDateTime.now()));
        addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.DOCUMENT_SIGNED);
    }

    public void signDocuments(Integer sesCode, String statementId) throws ValidationException {
        Statement statement = statementServiceDB.getStatementById(UUID.fromString(statementId));
        if (statement.getSesCode().equals(sesCode)) {
            EmailMessageDto emailMessageDto = createEmailMessageDto(
                    statementId,
                    EmailMessageDto.Theme.creditIssued,
                    "Документы Подписаны");
            kafkaService.sendCreditIssuedEmail(emailMessageDto);

            addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.CREDIT_ISSUED);

            Credit credit = creditServiceDB.getCreditById(statement.getCredit().getCreditId());
            credit.setCreditStatus(Credit.eCreditStatus.ISSUED);
            creditServiceDB.updateCredit(credit);
        } else {
            throw new ValidationException("Неверный код подтверждения");
        }
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
                .birthdate(client.getBirthDate().toLocalDate())
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

    private String getEmailByStatementId(UUID statementId) {
        return clientService.getClientById(statementServiceDB.getStatementById(statementId)
                        .getClient()
                        .getClientId())
                .getEmail();
    }

    private EmailMessageDto createEmailMessageDto(
            String statementId,
            EmailMessageDto.Theme theme,
            String messageText) {

        UUID statementIdUUID = UUID.fromString(statementId);
        String email = getEmailByStatementId(statementIdUUID);

        return EmailMessageDto.builder()
                .theme(theme)
                .address(email)
                .statementId(statementIdUUID)
                .text(messageText)
                .build();
    }

    private void processCalculationRequest(Statement statement, String statementId, ScoringDataDto scoringDataDto) throws FeignValidationException {
        try {
            CreditDto creditDto = calculatorFeignClient.getCreditDto(scoringDataDto);
            Credit credit = statement.getCredit();
            setCreditByCreditDto(credit, creditDto);
            creditServiceDB.updateCredit(credit);

            addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.DOCUMENT_CREATED);

            EmailMessageDto emailMessageDto = createEmailMessageDto(
                    statementId,
                    EmailMessageDto.Theme.createDocuments,
                    "Регистрация завершена");
            kafkaService.sendCreateDocumentsEmail(emailMessageDto);

            logger.info("Заявка одобрена, id: {}", statementId);
        } catch (FeignValidationException ex) {
            addStatementStatusAndUpdate(statement, Statement.eApplicationStatus.CLIENT_DENIED);

            StringBuilder message = new StringBuilder("Заявка отклонена, ниже - причины отказа.\n");
            ex.getErrors().forEach((error) -> {
                String errorMessage = error.getMessage();
                message.append(errorMessage).append("\n");
            });
            EmailMessageDto emailMessageDto = createEmailMessageDto(
                    statementId,
                    EmailMessageDto.Theme.statementDenied,
                    message.toString());
            kafkaService.sendStatementDeniedEmail(emailMessageDto);
            logger.info("Заявка отклонена, id: {}", statementId);
            throw ex;
        }
    }
}