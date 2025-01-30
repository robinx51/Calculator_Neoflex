package ru.gateway.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.library.dto.LoanOfferDto;
import ru.library.dto.LoanStatementRequestDto;
import ru.gateway.dto.StatementEntityDto;
import ru.library.dto.FinishRegistrationRequestDto;
import ru.library.exception.FeignValidationException;
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

    public StatementEntityDto getStatementById(String statementId) {
        logger.info("Запрос к MS Deal: /deal/admin/statement/{statementId}");
        return dealFeignClient.getStatement(statementId);
    }

    public List<StatementEntityDto> getStatements() {
        logger.info("Запрос к MS Deal: /deal/admin/statement");
        return dealFeignClient.getStatements();
    }
}