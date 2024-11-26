package MS_calculator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ScoringService {
    @Value("${calculator.baseRate}")
    private Integer baseRate;

    public BigDecimal evaluateTotalAmountByServices(BigDecimal amount, boolean isInsuranceEnabled) {
        if (isInsuranceEnabled)
            return amount.multiply(new BigDecimal("1.05"));
        else
            return amount;
    }

    public BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal rate = new BigDecimal(baseRate);
        if (isInsuranceEnabled) {
            rate = rate.subtract (new BigDecimal(3));
        }
        if (isSalaryClient) {
            rate = rate.subtract (new BigDecimal(1));
        }
        return rate;
    }

    public BigDecimal getMonthlyPayment(final BigDecimal totalAmount, final BigDecimal rate, final int term) {
        double ratioPayment = 0D;
        double monthlyRate = (rate.doubleValue() / 100) / 12;

        // К = (М * (1 + М) ^ S) / ((1 + М) ^ S — 1)
        // где М — месячная процентная ставка по кредиту, S — срок кредита в месяцах.
        ratioPayment = (monthlyRate * Math.pow((1 + monthlyRate), term)) / (Math.pow((1 + monthlyRate), term) - 1);
        // Х = С * К
        // где X — аннуитетный платеж, С — сумма кредита, К — коэффициент аннуитета.
        return new BigDecimal(totalAmount.doubleValue() * ratioPayment);
    }
}
