package MS_calculator.DTO;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CreditDto {
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private List<PaymentScheduleElementDto> paymentSchedule;

    public CreditDto setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public CreditDto setTerm(Integer term) {
        this.term = term;
        return this;
    }

    public CreditDto setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
        return this;
    }

    public CreditDto setRate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public CreditDto setPsk(BigDecimal psk) {
        this.psk = psk;
        return this;
    }

    public CreditDto setInsuranceEnabled(Boolean insuranceEnabled) {
        isInsuranceEnabled = insuranceEnabled;
        return this;
    }

    public CreditDto setSalaryClient(Boolean salaryClient) {
        isSalaryClient = salaryClient;
        return this;
    }

    public CreditDto setPaymentSchedule(List<PaymentScheduleElementDto> paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
        return this;
    }
}
