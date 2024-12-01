package MS_calculator;

import MS_calculator.DTO.*;
import MS_calculator.Services.CalcService;
import MS_calculator.Services.OfferService;
import MS_calculator.Services.ScoringService;
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
    public List<LoanOfferDto> Offers(@RequestBody @Validated LoanStatementRequestDto request) {
        logger.info("Обработка запроса на рассчёт кредита: сумма = {}, срок = {}, фамилия = {}, имя = {}",
                request.getAmount(),
                request.getTerm(),
                request.getLastName(),
                request.getFirstName());
        List<LoanOfferDto> offers = offerService.generateOffers(request);
        logger.info("Обработан запрос на рассчёт кредита(лучшее предложение): срок = {}, запрошенная сумма = {}, ставка = {}, общая сумма = {}, ежемесячный платёж = {}",
                offers.getFirst().getTerm(),
                offers.getFirst().getRequestedAmount(),
                offers.getFirst().getRate(),
                offers.getFirst().getTotalAmount(),
                offers.getFirst().getMonthlyPayment());
        return offers;
    }

    @PostMapping("/calc")
    @Tag(   name = "Валидация присланных данных + полный расчет параметров кредита",
            description = "Происходит скоринг данных, высчитывание итоговой ставки(rate), полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей (List<PaymentScheduleElementDto>)")
    public CreditDto Calc(@RequestBody @Validated ScoringDataDto request) {
        logger.info("Получен запрос на кредит: сумма = {}, срок = {}, фамилия = {}, имя = {}, статус занятости = {}, необходимость страховки = {}.",
                request.getAmount(),
                request.getTerm(),
                request.getLastName(),
                request.getFirstName(),
                request.getEmployment().getEmploymentStatus(),
                request.getIsInsuranceEnabled());
        CreditDto creditDto = calcService.generateCredit(request);
        logger.info("Обработана заявка на кредит: сумма = {}, срок = {}, ставка = {}, ежемесячный платёж = {}, ПСК = {}",
                creditDto.getAmount(),
                creditDto.getTerm(),
                creditDto.getRate(),
                creditDto.getMonthlyPayment(),
                creditDto.getPsk());
        return creditDto;
    }
}
