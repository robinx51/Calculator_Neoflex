package MS_calculator.DTO;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class PaymentScheduleElementDto {
    private Integer number;
    private LocalDate date;
    // Общая сумма платежа
    private BigDecimal totalPayment;
    // Выплата процентов
    private BigDecimal interestPayment;
    // Выплата долга
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;

    public PaymentScheduleElementDto setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public PaymentScheduleElementDto setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public PaymentScheduleElementDto setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
        return this;
    }

    public PaymentScheduleElementDto setInterestPayment(BigDecimal interestPayment) {
        this.interestPayment = interestPayment;
        return this;
    }

    public PaymentScheduleElementDto setDebtPayment(BigDecimal debtPayment) {
        this.debtPayment = debtPayment;
        return this;
    }

    public PaymentScheduleElementDto setRemainingDebt(BigDecimal remainingDebt) {
        this.remainingDebt = remainingDebt;
        return this;
    }
}
