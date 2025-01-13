package ru.statement.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;
import ru.statement.feignClient.DealFeignClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {
    private final DealFeignClient dealFeignClient;
    private static final Logger logger = LoggerFactory.getLogger(StatementService.class);

    public List<LoanOfferDto> processClient(LoanStatementRequestDto request) {
        logger.info("Получена заявка на расчёт возможных условий кредита");
        return dealFeignClient.getOffers(request);
    }

    public void selectOffer(LoanOfferDto request) {
        logger.info("Получен запрос на выбор кредитного предложения");
        dealFeignClient.selectOffer(request);
        logger.info("Запрос на выбор предложения обработан");
    }
}