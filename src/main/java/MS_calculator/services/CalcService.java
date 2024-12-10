package MS_calculator.services;

import MS_calculator.dto.CreditDto;
import MS_calculator.dto.ScoringDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CalcService {
    private final ScoringService scoringService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CalcService.class);

    public CreditDto generateCredit(ScoringDataDto request) {
        logger.info("Получен запрос на кредит: сумма = {}, срок = {}, фамилия = {}, имя = {}, статус занятости = {}, необходимость страховки = {}.",
                request.getAmount(),
                request.getTerm(),
                request.getLastName(),
                request.getFirstName(),
                request.getEmployment().getEmploymentStatus(),
                request.getIsInsuranceEnabled());

        BigDecimal rate = scoringService.calculateFinalRate(request);
        BigDecimal totalAmount = scoringService.evaluateTotalAmountByServices(request.getAmount(), request.getIsInsuranceEnabled());
        BigDecimal monthlyPayment = scoringService.getMonthlyPayment(totalAmount, rate, request.getTerm());
        CreditDto creditDto = CreditDto.builder()
                .amount(totalAmount)
                .term(request.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(scoringService.calculatePsk(monthlyPayment, request.getTerm()))
                .isInsuranceEnabled(request.getIsInsuranceEnabled())
                .isSalaryClient(request.getIsSalaryClient())
                .paymentSchedule(scoringService.calculatePaymentSchedule(totalAmount, request.getTerm(), rate, monthlyPayment))
                .build();

        logger.info("Обработана заявка на кредит: сумма = {}, срок = {}, ставка = {}, ежемесячный платёж = {}, ПСК = {}",
                creditDto.getAmount(),
                creditDto.getTerm(),
                creditDto.getRate(),
                creditDto.getMonthlyPayment(),
                creditDto.getPsk());
        return creditDto;
    }
}
