package MS_calculator.Services;

import MS_calculator.DTO.PaymentScheduleElementDto;
import MS_calculator.DTO.ScoringDataDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service @Setter @Getter
public class ScoringService {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ScoringService.class);
    @Value("${calculator.baseRate}")
    private int baseRate;

    public BigDecimal evaluateTotalAmountByServices(BigDecimal amount, boolean isInsuranceEnabled) {
        if (isInsuranceEnabled)
            return amount.multiply(new BigDecimal("1.05"));
        else
            return amount;
    }

    public BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        int rate = getBaseRate();
        if (isInsuranceEnabled) {
            rate = rate - 3;
        }
        if (isSalaryClient) {
            rate = rate - 1;
        }
        return new BigDecimal(rate);
    }

    public BigDecimal getMonthlyPayment(final BigDecimal totalAmount, final BigDecimal rate, final int term) {
        double ratioPayment;
        double monthlyRate = (rate.doubleValue() / 100) / 12;

        // К = (М * (1 + М) ^ S) / ((1 + М) ^ S — 1)
        // где М — месячная процентная ставка по кредиту, S — срок кредита в месяцах.
        ratioPayment = (monthlyRate * Math.pow((1 + monthlyRate), term)) / (Math.pow((1 + monthlyRate), term) - 1);
        logger.debug("Коэффициент аннуитета: {}", ratioPayment);
        // Х = С * К
        // где X — аннуитетный платеж, С — сумма кредита, К — коэффициент аннуитета.
        return new BigDecimal(totalAmount.doubleValue() * ratioPayment);
    }

    public BigDecimal calculateFinalRate(ScoringDataDto request) {
        int rate = baseRate;
        switch (request.getEmployment().getEmploymentStatus()) {
            case SELF_EMPLOYED -> rate += 2;
            case BUSINESS_OWNER -> rate += 1;
        } switch (request.getEmployment().getPosition()) {
            case MIDDLE_MANAGER -> rate -= 2;
            case TOP_MANAGER -> rate -= 3;
        } switch (request.getMaritalStatus()) {
            case MARRIED -> rate -= 3;
            case DIVORCED -> rate += 1;
        } switch (request.getGender()) {
            case MALE -> {
                if (getYears(request.getBirthdate())>= 30 && getYears(request.getBirthdate()) <= 50)
                    rate -= 3;
            } case FEMALE -> {
                if (getYears(request.getBirthdate())>= 32 && getYears(request.getBirthdate()) <= 60)
                    rate -= 3;
            } case NON_BINARY -> rate += 7;
        }
        return new BigDecimal(rate);
    }

    public List<PaymentScheduleElementDto> calculatePaymentSchedule (BigDecimal totalAmount, int term, BigDecimal rate, BigDecimal monthlyPayment) {
        LocalDate date;
        BigDecimal interestPayment; // Выплата процентов
        BigDecimal debtPayment;    // Выплата долга
        BigDecimal remainingDebt = totalAmount;     // Оставшийся долг

        double monthlyRate = (rate.doubleValue() / 100) / 12;

        List<PaymentScheduleElementDto> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 1; i <= term; i++) {
            date = today.plusMonths(i);
            interestPayment = remainingDebt.multiply(BigDecimal.valueOf(monthlyRate)) ;
            debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            list.add(new PaymentScheduleElementDto()
                    .setNumber(i)
                    .setDate(date)
                    .setTotalPayment(monthlyPayment)
                    .setInterestPayment(interestPayment)
                    .setDebtPayment(debtPayment)
                    .setRemainingDebt(remainingDebt));
        }
        list.getLast().setRemainingDebt(new BigDecimal(0));
        return list;
    }

    public BigDecimal calculatePsk(BigDecimal monthlyPayment, Integer term) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }

    private int getYears(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

}
