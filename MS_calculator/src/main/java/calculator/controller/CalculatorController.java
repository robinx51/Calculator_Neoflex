package calculator.controller;

import calculator.dto.CreditDto;
import calculator.dto.LoanOfferDto;
import calculator.dto.LoanStatementRequestDto;
import calculator.dto.ScoringDataDto;
import calculator.services.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private final OfferService offerService;
    private final CalcService calcService;

    @Autowired
    public CalculatorController(ScoringService scoringService) {
        this.offerService = new OfferService(scoringService);
        this.calcService = new CalcService(scoringService);
    }

    @PostMapping("/offers")
    @Tag(   name = "Расчёт возможных условий кредита",
            description = "На основании LoanStatementRequestDto происходит" +
            " прескоринг, создаётся 4 кредитных предложения LoanOfferDto на основании всех возможных комбинаций булевских полей " +
            "isInsuranceEnabled и isSalaryClient")
    public List<LoanOfferDto> createOffers(@RequestBody @Validated LoanStatementRequestDto request) {
        return offerService.generateOffers(request);
    }

    @PostMapping("/calc")
    @Tag(   name = "Валидация присланных данных + полный расчет параметров кредита",
            description = "Происходит скоринг данных, высчитывание итоговой ставки(rate), полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей (List<PaymentScheduleElementDto>)")
    public CreditDto calculateCredit(@RequestBody @Validated ScoringDataDto request) {
        return calcService.generateCredit(request);
    }
}
