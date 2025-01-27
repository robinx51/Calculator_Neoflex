package ru.deal.FeignClient;

import ru.calculator.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.deal.exception.FeignValidationException;

import java.util.List;

@FeignClient(name = "calculator-calculator.neoflex.service", url = "${calculator.url}")
public interface CalculatorFeignClient {

    @PostMapping("/calculator/offers")
    List<LoanOfferDto> getOffers(@RequestBody LoanStatementRequestDto request);
    @PostMapping("/calculator/calc")
    CreditDto getCreditDto(@RequestBody ScoringDataDto request) throws FeignValidationException;
}
