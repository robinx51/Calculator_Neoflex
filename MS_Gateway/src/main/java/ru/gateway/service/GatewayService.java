package ru.gateway.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;
import ru.deal.db_pgsql.entity.Statement;
import ru.deal.dto.FinishRegistrationRequestDto;
import ru.deal.exception.FeignValidationException;
import ru.gateway.FeignClient.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GatewayService {
    private final DealFeignClient dealFeignClient;
    private final StatementFeignClient statementFeignClient;

    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

    public List<LoanOfferDto> initialRegistration(LoanStatementRequestDto request) {
        logger.info("Запрос к MS Statement: /statement");
        return statementFeignClient.initialRegistration(request);
    }

    public void selectOffer(LoanOfferDto request) {
        logger.info("Запрос к MS Statement: /statement/offer");
        statementFeignClient.selectOffer(request);
    }

    public void finishRegistration(String statementId, FinishRegistrationRequestDto request) throws FeignValidationException {
        logger.info("Запрос к MS Deal: /deal/calculate/{statementId}");
        dealFeignClient.finishRegistration(statementId, request);
    }

    public void sendDocuments(String statementId) {
        logger.info("Запрос к MS Deal: /deal/document/{statementId}/send");
        dealFeignClient.createDocuments(statementId);
    }

    public void signRequestDocuments(String statementId) {
        logger.info("Запрос к MS Deal: /deal/document/{statementId}/sign");
        dealFeignClient.signRequestDocuments(statementId);
    }

    public void signDocuments(String statementId, Integer sesCode) {
        logger.info("Запрос к MS Deal: /deal/document/{statementId}/code");
        dealFeignClient.signDocuments(statementId, sesCode);
    }

    public Statement getStatementById(String statementId) {
        logger.info("Запрос к MS Deal: /deal/admin/statement/{statementId}");
        return dealFeignClient.getStatement(statementId);
    }

    public List<Statement> getStatements() {
        logger.info("Запрос к MS Deal: /deal/admin/statement");
        return dealFeignClient.getStatements();
    }
}