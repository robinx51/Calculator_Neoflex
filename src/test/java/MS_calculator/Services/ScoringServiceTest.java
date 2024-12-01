package MS_calculator.Services;

import MS_calculator.DTO.EmploymentDto;
import MS_calculator.DTO.PaymentScheduleElementDto;
import MS_calculator.DTO.ScoringDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

        assertEquals(monthlyPayment, (int)Math.round(scoringService.getMonthlyPayment(totalAmount, rate, 60).doubleValue()));
        assertThrows(NullPointerException.class, () -> scoringService.getMonthlyPayment(null, rate, 6));
        assertThrows(NullPointerException.class, () -> scoringService.getMonthlyPayment(totalAmount, null, 6));
    }

    @Test
    void calculateFinalRate() {
        EmploymentDto employmentDto1 = new EmploymentDto()
                .setEmploymentStatus(EmploymentDto.EmploymentStatus.BUSINESS_OWNER) // +1
                .setPosition(EmploymentDto.Position.TOP_MANAGER);                   // -3
        ScoringDataDto request1 = new ScoringDataDto()
                .setEmployment(employmentDto1)
                .setMaritalStatus(ScoringDataDto.MaritalStatus.MARRIED)             // -3
                .setGender(ScoringDataDto.Gender.MALE)
                .setBirthdate(LocalDate.now().minusYears(30));        // -3

        EmploymentDto employmentDto2 = new EmploymentDto()
                .setEmploymentStatus(EmploymentDto.EmploymentStatus.SELF_EMPLOYED) // +2
                .setPosition(EmploymentDto.Position.MIDDLE_MANAGER);               // -2
        ScoringDataDto request2 = new ScoringDataDto()
                .setEmployment(employmentDto2)
                .setMaritalStatus(ScoringDataDto.MaritalStatus.DIVORCED)           // +1
                .setGender(ScoringDataDto.Gender.FEMALE)
                .setBirthdate(LocalDate.now().minusYears(32));        // -3

        assertEquals(12, scoringService.calculateFinalRate(request1).intValue());
        assertEquals(18, scoringService.calculateFinalRate(request2).intValue());

        request2.setGender(ScoringDataDto.Gender.NON_BINARY);                      // +10
        assertEquals(28, scoringService.calculateFinalRate(request2).intValue());
    }

    @Test
    void  calculatePaymentSchedule() {
        BigDecimal totalAmount = new BigDecimal("1000000");
        int term = 6;
        BigDecimal rate = new BigDecimal("21");
        BigDecimal monthlyPayment = new BigDecimal(177023);

        List<PaymentScheduleElementDto> list = scoringService.calculatePaymentSchedule(totalAmount, term, rate, monthlyPayment);

        assertEquals(term, list.size());
        assertEquals(0, list.getLast().getRemainingDebt().intValue());
        assertEquals(monthlyPayment, list.getLast().getTotalPayment());
    }

    @Test
    void calculatePsk() {
        BigDecimal monthlyPayment = new BigDecimal(177023);
        int term = 6;

        assertEquals(monthlyPayment.multiply(BigDecimal.valueOf(term)), scoringService.calculatePsk(monthlyPayment, term));
    }
}