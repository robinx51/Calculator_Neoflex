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
        if (request.getGender() == ScoringDataDto.Gender.MALE) {
            if (Period.between(request.getBirthdate(), LocalDate.now()).getYears() >= 32
                && Period.between(request.getBirthdate(), LocalDate.now()).getYears() <= 60 )
                rate -= 3;
        }
        return new BigDecimal(rate);
    }

    public List<PaymentScheduleElementDto> calculatePaymentSchedule (ScoringDataDto request) {
        List<PaymentScheduleElementDto> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 1; i <= request.getTerm(); i++) {
            LocalDate date = today.plusMonths(i);
            list.add(new PaymentScheduleElementDto()
                    .setNumber(i)
                    .setDate(date));
        }
        return null;
    }

    public BigDecimal calculatePsk(BigDecimal totalAmount, BigDecimal rate, Integer term) {

        return null;
    }
    private int getYears(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

}
