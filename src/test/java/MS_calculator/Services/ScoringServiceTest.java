package MS_calculator.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {
    @InjectMocks
    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        scoringService.setBaseRate(20);
    }

    @Test
    void evaluateTotalAmountByServices() {
        BigDecimal amount = new BigDecimal("100000");

        assertEquals(new BigDecimal("105000.00"), scoringService.evaluateTotalAmountByServices(amount, true));
        assertEquals(amount, scoringService.evaluateTotalAmountByServices(amount, false));
        assertThrows(NullPointerException.class, () -> scoringService.evaluateTotalAmountByServices(null, true));
    }

    @Test
    void calculateRate() {
        assertEquals(new BigDecimal("16"), scoringService.calculateRate(true, true));
        assertEquals(new BigDecimal("17"), scoringService.calculateRate(true, false));
        assertEquals(new BigDecimal("19"), scoringService.calculateRate(false, true));
        assertEquals(new BigDecimal("20"), scoringService.calculateRate(false, false));
    }

    @Test
    void getMonthlyPayment() {
        BigDecimal totalAmount = new BigDecimal("2000000");
        BigDecimal rate = new BigDecimal("15");
        int monthlyPayment = 47580;

        //String formattedResult = new DecimalFormat("#0.00").format(scoringService.getMonthlyPayment(totalAmount, rate, 6).intValue());

        assertEquals(monthlyPayment, (int)Math.round(scoringService.getMonthlyPayment(totalAmount, rate, 60).doubleValue()));
        assertThrows(NullPointerException.class, () -> scoringService.getMonthlyPayment(null, rate, 6));
        assertThrows(NullPointerException.class, () -> scoringService.getMonthlyPayment(totalAmount, null, 6));
    }
}