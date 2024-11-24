package MS_calculator;

import MS_calculator.DTO.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CalculatorController.class);
    @Value("${calculator.baseRate}")
    private Integer baseRate;

    @PostMapping("/offers")
    @Tag(   name = "Расчёт возможных условий кредита",
            description = "На основании LoanStatementRequestDto происходит" +
            " прескоринг, создаётся 4 кредитных предложения LoanOfferDto на основании всех возможных комбинаций булевских полей " +
            "isInsuranceEnabled и isSalaryClient")
    public List<LoanOfferDto> Offers (@RequestBody LoanStatementRequestDto request) {
        logger.info("Received request for offers: {}", request);

        List<LoanOfferDto> offers = PreScoring(request);
        logger.info("Generated offers: {}", offers);

        return offers;
    }
    @PostMapping("/calc")
    @Tag(   name = "Валидация присланных данных + полный расчет параметров кредита",
            description = "Происходит скоринг данных," +
            " высчитывание итоговой ставки(rate), полной стоимости кредита(psk), размер ежемесячного платежа(monthlyPayment), " +
            "график ежемесячных платежей (List<PaymentScheduleElementDto>)")
    public CreditDto Calc (@RequestBody ScoringDataDto scoringDataDto) {

        return null;
    }

    private List<LoanOfferDto> PreScoring (LoanStatementRequestDto request) {
        if (    CheckNames(request.getFirstName(), request.getLastName(), request.getMiddleName())
                && request.getAmount().compareTo(new BigDecimal(20000)) >= 0
                && request.getTerm() >= 6
                && Period.between(request.getBirthdate().plusDays(1), LocalDate.now()).getYears() >= 18
                && Pattern.matches("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$", request.getEmail())
                && request.getPassportSeries().length() == 4
                && request.getPassportNumber().length() == 6 ) {
            return GenerateOffers(request);
        } else
            throw new NullPointerException("Прескоринг не пройден");
    }
    private boolean CheckNames(String firstName, String lastName, String middleName) {
        List<String> names = new ArrayList<>(Arrays.asList(firstName, lastName, middleName));
        
        for (byte i = 0; i < 3; i++) {
            if (Pattern.matches("^[a-zA-Z]{2,30}$", names.get(i)))
                continue;
            else {
                if (i == 2 && names.get(i).isEmpty())
                    continue;
                else
                    return false;
            }
        }
        return true;
    }
    private List<LoanOfferDto> GenerateOffers(LoanStatementRequestDto request) {
        List<LoanOfferDto> offers = new ArrayList<>();
        int counter;

        for (int insurance = 0; insurance < 2; insurance++){
            for (int salary = 0; salary < 2; salary++) {
                double insuranceAmount = 0L, rate = baseRate;
                boolean isInsurance = false, isSalary = false;
                if (insurance == 1) {
                    isInsurance = true;
                    insuranceAmount = request.getAmount().doubleValue() * 0.05;
                    rate -= 3;
                }
                if (salary == 1) {
                    isSalary = true;
                    rate -= 1;
                }
                BigDecimal totalAmount = request.getAmount().add(new BigDecimal(insuranceAmount));
                offers.add( new LoanOfferDto(UUID.randomUUID(), request.getAmount(),
                        totalAmount, request.getTerm(), GetMonthlyPayment(totalAmount, rate, request.getTerm()),
                        new BigDecimal(rate), isInsurance, isSalary));
            }
        }
        Collections.sort(offers);
        return offers;
    }
    private BigDecimal GetMonthlyPayment(final BigDecimal totalAmount, final double rate, final int term) {
        double ratioPayment = 0D;
        double monthlyRate = (rate / 100) / 12;

        // К = (М * (1 + М) ^ S) / ((1 + М) ^ S — 1)
        // где М — месячная процентная ставка по кредиту, S — срок кредита в месяцах.
        ratioPayment = (monthlyRate * Math.pow((1 + monthlyRate), term)) / (Math.pow((1 + monthlyRate), term) - 1);
        // Х = С * К
        // где X — аннуитетный платеж, С — сумма кредита, К — коэффициент аннуитета.
        return new BigDecimal(totalAmount.doubleValue() * ratioPayment);
    }
}
