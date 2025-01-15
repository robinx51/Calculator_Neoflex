package ru.deal.FeignClient;

import ru.calculator.dto.CreditDto;
import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;
import ru.calculator.dto.ScoringDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "calculator-calculator.neoflex.service", url = "${calculator.url}")
public interface CalculatorFeignClient {

    @PostMapping("/calculator/offers")
    List<LoanOfferDto> getOffers(@RequestBody LoanStatementRequestDto request);
    @PostMapping("/calculator/calc")
    CreditDto getCreditDto(@RequestBody ScoringDataDto request);
}
