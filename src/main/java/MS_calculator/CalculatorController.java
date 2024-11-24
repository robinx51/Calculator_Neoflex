package MS_calculator;


import MS_calculator.DTO.*;
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
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;
    @PostMapping("/offers")
    public List<LoanOfferDto> Offers (@RequestBody LoanStatementRequestDto request) {
        return PreScoring(request);
    }
    @PostMapping("/calc")
    public CreditDto Calc (@RequestBody ScoringDataDto scoringDataDto) {

        return null;
    }

    private List<LoanOfferDto> PreScoring (LoanStatementRequestDto request) {
        if (    CheckNames(request.getFirstName(), request.getLastName(), request.getMiddleName())
                && request.getAmount().compareTo(new BigDecimal(20000)) >= 0
                && request.getTerm() >= 6
                && Period.between(request.getBirthdate().plusDays(1), LocalDate.now()).getYears() >= 18
                && Pattern.matches("^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$", request.getEmail())
                && request.getPassportNumber().length() == 4
                && request.getPassportSeries().length() == 6 ) {
            return GenerateOffers(request);
        } else
            return new ArrayList<LoanOfferDto>();
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
        //boolean isInsurance = true, isSalary = true;
        int counter;

        for (int insurance = 0; insurance < 2; insurance++){
            for (int salary = 0; salary < 2; salary++) {
                double insuranceAmount = 0L, rate = 0L;
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
                /*offers.add( new LoanOfferDto(UUID.randomUUID(), request.getAmount(),
                        totalAmount, request.getTerm(), ,
                        rate, isInsurance, isSalary));*/
            }
        }
        Collections.sort(offers);
        return offers;
    }
}
