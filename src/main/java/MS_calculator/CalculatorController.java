package MS_calculator;

import MS_calculator.DTO.*;
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
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CalculatorController.class);

    private final OfferService offerService;

    @Autowired
    public CalculatorController(ScoringService scoringService) {
        this.offerService = new OfferService(scoringService);
    }

    @PostMapping("/offers")
    @Tag(   name = "Расчёт возможных условий кредита",
            description = "На основании LoanStatementRequestDto происходит" +
            " прескоринг, создаётся 4 кредитных предложения LoanOfferDto на основании всех возможных комбинаций булевских полей " +
            "isInsuranceEnabled и isSalaryClient")
    public List<LoanOfferDto> Offers(@RequestBody @Validated LoanStatementRequestDto request) {
        logger.info("Received request for offers: {}", request);
        return offerService.generateOffers(request);
    }
    @PostMapping("/calc")
    @Tag(   name = "Валидация присланных данных + полный расчет параметров кредита",
            description = "Происходит скоринг данных," +
            " высчитывание итоговой ставки(rate), полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), " +
            "график ежемесячных платежей (List<PaymentScheduleElementDto>)")
    public CreditDto Calc(@RequestBody ScoringDataDto scoringDataDto) {

        return null;
    }
}
