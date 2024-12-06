package MS_calculator.Services;

import MS_calculator.DTO.CreditDto;
import MS_calculator.DTO.ScoringDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CalcService {
    private final ScoringService scoringService;

    public CreditDto generateCredit(ScoringDataDto request) {
        BigDecimal rate = scoringService.calculateFinalRate(request);
        BigDecimal totalAmount = scoringService.evaluateTotalAmountByServices(request.getAmount(), request.getIsInsuranceEnabled());
        BigDecimal monthlyPayment = scoringService.getMonthlyPayment(totalAmount, rate, request.getTerm());

        return new CreditDto()
                .setAmount(totalAmount)
                .setTerm(request.getTerm())
                .setMonthlyPayment(monthlyPayment)
                .setRate(rate)
                .setPsk(scoringService.calculatePsk(monthlyPayment, request.getTerm()))
                .setInsuranceEnabled(request.getIsInsuranceEnabled())
                .setSalaryClient(request.getIsSalaryClient())
                .setPaymentSchedule(scoringService.calculatePaymentSchedule(totalAmount, request.getTerm(), rate, monthlyPayment));
    }
}
