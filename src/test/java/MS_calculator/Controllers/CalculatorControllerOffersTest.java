package MS_calculator.Controllers;

import MS_calculator.CalculatorController;
import MS_calculator.DTO.LoanOfferDto;
import MS_calculator.DTO.LoanStatementRequestDto;
import MS_calculator.Services.CalcService;
import MS_calculator.Services.OfferService;
import MS_calculator.Services.ScoringService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculatorControllerOffersTest {
    @Mock
    private ScoringService scoringService;

    @InjectMocks
    private CalculatorController calculatorController;

    private Validator validator;
    private static LoanStatementRequestDto validRequest;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    public static void setUpRequest() {
         validRequest = new LoanStatementRequestDto()
                .setAmount(BigDecimal.valueOf(20000))
                .setTerm(6)
                .setFirstName("John")
                .setLastName("Doe")
                .setMiddleName("Smith")
                .setEmail("smith@example.com")
                .setBirthdate(LocalDate.now().minusYears(18))
                .setPassportSeries("1234")
                .setPassportNumber("123456");
    }

    @Test
    void testValidLoanRequestDto() {
        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(validRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    public void testInvalidLoanRequestWithNulls() {
        LoanStatementRequestDto dto = new LoanStatementRequestDto();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(dto);

        for (ConstraintViolation<LoanStatementRequestDto> violation : violations) {
            System.out.println("Поле: " + violation.getPropertyPath() + ", Ошибка: " + violation.getMessage());
        }

        assertEquals(8, violations.size());
    }

    @Test
    public void testInvalidLoanRequestIncorrectValues() {
        LoanStatementRequestDto dto = new LoanStatementRequestDto()
                .setAmount(BigDecimal.valueOf(19999))
                .setTerm(5)
                .setFirstName("John1")
                .setLastName("Doe1")
                .setMiddleName("Smith1")
                .setEmail("email")
                .setBirthdate(LocalDate.now().minusYears(17))
                .setPassportSeries("123")
                .setPassportNumber("1234567");

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(dto);

        assertEquals(9, violations.size());
    }

    @Test
    void testResponseCreditDto() {
        when(scoringService.calculateRate(anyBoolean(), anyBoolean())).thenReturn(new BigDecimal("20"));
        List<LoanOfferDto> offers = calculatorController.Offers(validRequest);
        assertEquals(validRequest.getTerm(), offers.getFirst().getTerm());
        assertEquals(validRequest.getAmount(), offers.getFirst().getRequestedAmount());
        assertEquals(4, offers.size());
    }
}
