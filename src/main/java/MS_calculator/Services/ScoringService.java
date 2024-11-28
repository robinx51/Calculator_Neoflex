package MS_calculator.Services;

import MS_calculator.DTO.ScoringDataDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

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

        return new BigDecimal("20");
    }
    // Рабочий статус: Самозанятый → ставка увеличивается на 2; Владелец бизнеса → ставка увеличивается на 1
    // Позиция на работе: Менеджер среднего звена → ставка уменьшается на 2; Топ-менеджер → ставка уменьшается на 3
    // Семейное положение: Замужем/женат → ставка уменьшается на 3; Разведен → ставка увеличивается на 1
    // Пол: Женщина, возраст от 32 до 60 лет → ставка уменьшается на 3; Мужчина, возраст от 30 до 55 лет → ставка уменьшается на 3; Не бинарный → ставка увеличивается на 7
}
