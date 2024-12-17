package MS_calculator.controllers;

import calculator.controller.CalculatorController;
import calculator.dto.LoanOfferDto;
import calculator.dto.LoanStatementRequestDto;
import calculator.services.ScoringService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
    private static final LoanStatementRequestDto validRequest = LoanStatementRequestDto.builder()
            .amount(BigDecimal.valueOf(20000))
            .term(6)
            .firstName("John")
            .lastName("Doe")
            .middleName("Smith")
            .email("smith@example.com")
            .birthdate(LocalDate.now().minusYears(18))
            .passportSeries("1234")
            .passportNumber("123456")
            .build();

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void testInvalidLoanRequestWithNulls() {
        LoanStatementRequestDto dto = LoanStatementRequestDto.builder().build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(dto);

        assertEquals(8, violations.size());
    }

    @Test
    public void testInvalidLoanRequestIncorrectValues() {
        LoanStatementRequestDto dto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(19999))
                .term(5)
                .firstName("John1")
                .lastName("Doe1")
                .middleName("Smith1")
                .email("email")
                .birthdate(LocalDate.now().minusYears(17))
                .passportSeries("123")
                .passportNumber("1234567")
                .build();

        Set<ConstraintViolation<LoanStatementRequestDto>> violations = validator.validate(dto);

        assertEquals(9, violations.size());
    }

    @Test
    void testResponseCreditDto() {
        when(scoringService.calculateRate(anyBoolean(), anyBoolean())).thenReturn(new BigDecimal("20"));
        List<LoanOfferDto> offers = calculatorController.createOffers(validRequest);
        assertEquals(validRequest.getTerm(), offers.get(0).getTerm());
        assertEquals(validRequest.getAmount(), offers.get(0).getRequestedAmount());
        assertEquals(4, offers.size());
    }
}
