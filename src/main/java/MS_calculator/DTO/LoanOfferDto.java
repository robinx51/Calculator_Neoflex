package MS_calculator.DTO;

import lombok.Data;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Data
public class LoanOfferDto implements Comparable<LoanOfferDto> {
    private UUID statementId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private boolean isInsuranceEnabled;
    private boolean isSalaryClient;

    public LoanOfferDto setStatementId(UUID statementId) {
        this.statementId = statementId;
        return this;
    }

    public LoanOfferDto setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
        return this;
    }

    public LoanOfferDto setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public LoanOfferDto setTerm(Integer term) {
        this.term = term;
        return this;
    }

    public LoanOfferDto setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
        return this;
    }

    public LoanOfferDto setRate(BigDecimal rate) {
        this.rate = rate;
        return this;
    }

    public LoanOfferDto setInsuranceEnabled(boolean insuranceEnabled) {
        isInsuranceEnabled = insuranceEnabled;
        return this;
    }

    public LoanOfferDto setSalaryClient(boolean salaryClient) {
        isSalaryClient = salaryClient;
        return this;
    }

    @Override
    public int compareTo(LoanOfferDto other) {
        return this.rate.compareTo(other.rate);
    }

}
