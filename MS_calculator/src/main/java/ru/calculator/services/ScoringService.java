package ru.calculator.services;

import ru.calculator.dto.PaymentScheduleElementDto;
import ru.calculator.dto.ScoringDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScoringService {
    private static final Logger logger = LoggerFactory.getLogger(ScoringService.class);

    @Value("${calculator.baseRate}")
    private int baseRate;

    private static final double ratioInsurance = 1.05;

    public BigDecimal evaluateTotalAmountByServices(BigDecimal amount, boolean isInsuranceEnabled) {
        if (isInsuranceEnabled)
            return amount.multiply(new BigDecimal(ratioInsurance)).setScale(2, RoundingMode.HALF_UP);
        else
            return amount;
    }

    public BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        int rate = baseRate;
        if (isInsuranceEnabled) {
            rate = rate - 3;
        }
        if (isSalaryClient) {
            rate = rate - 1;
        }
        return new BigDecimal(rate);
    }

    public BigDecimal getMonthlyPayment(final BigDecimal totalAmount, final BigDecimal rate, final int term) {
        double monthlyRate = (rate.doubleValue() / 100) / 12;

        // К = (М * (1 + М) ^ S) / ((1 + М) ^ S — 1)
        // где М — месячная процентная ставка по кредиту, S — срок кредита в месяцах.
        double ratioPayment = (monthlyRate * Math.pow((1 + monthlyRate), term)) / (Math.pow((1 + monthlyRate), term) - 1);
        logger.debug("Коэффициент аннуитета: {}", ratioPayment);
        // Х = С * К
        // где X — аннуитетный платеж, С — сумма кредита, К — коэффициент аннуитета.
        return new BigDecimal(totalAmount.doubleValue() * ratioPayment).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateFinalRate(ScoringDataDto request) {
        BigDecimal rate = new BigDecimal(baseRate);
        switch (request.getEmployment().getEmploymentStatus()) {
            case SELF_EMPLOYED ->        rate = rate.add(BigDecimal.valueOf(2));
            case BUSINESS_OWNER ->       rate = rate.add(BigDecimal.valueOf(1));
        } switch (request.getEmployment().getPosition()) {
            case MIDDLE_MANAGER ->  rate = rate.subtract(BigDecimal.valueOf(2));
            case TOP_MANAGER ->     rate = rate.subtract(BigDecimal.valueOf(3));
        } switch (request.getMaritalStatus()) {
            case MARRIED ->         rate = rate.subtract(BigDecimal.valueOf(3));
            case DIVORCED ->             rate = rate.add(BigDecimal.valueOf(1));
        } switch (request.getGender()) {
            case MALE -> {
                if (getYears(request.getBirthdate())>= 30 && getYears(request.getBirthdate()) <= 50)
                                    rate = rate.subtract(BigDecimal.valueOf(3));
            } case FEMALE -> {
                if (getYears(request.getBirthdate())>= 32 && getYears(request.getBirthdate()) <= 60)
                                    rate = rate.subtract(BigDecimal.valueOf(3));
            } case NON_BINARY ->         rate = rate.add(BigDecimal.valueOf(7));
        }
        return rate;
    }

    public List<PaymentScheduleElementDto> calculatePaymentSchedule (BigDecimal totalAmount, int term, BigDecimal rate, BigDecimal monthlyPayment) {
        BigDecimal interestPayment;             // Выплата процентов
        BigDecimal debtPayment;                 // Выплата долга
        BigDecimal remainingDebt = totalAmount; // Оставшийся долг

        double monthlyRate = (rate.doubleValue() / 100) / 12;

        List<PaymentScheduleElementDto> list = new ArrayList<>();
        for (int i = 1; i <= term; i++) {
            interestPayment = remainingDebt.multiply(BigDecimal.valueOf(monthlyRate)).setScale(2, RoundingMode.HALF_UP);
            debtPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
            remainingDebt = remainingDebt.subtract(debtPayment).setScale(2, RoundingMode.HALF_UP);

            list.add(PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(LocalDate.now().plusMonths(i))
                    .totalPayment(monthlyPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt).build());
        }
        list.get(list.size() - 1).setRemainingDebt(BigDecimal.ZERO);
        return list;
    }

    public BigDecimal calculatePsk(BigDecimal monthlyPayment, Integer term) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }

    private int getYears(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    public void setBaseRate(int baseRate) {
        this.baseRate = baseRate;
    }
}
