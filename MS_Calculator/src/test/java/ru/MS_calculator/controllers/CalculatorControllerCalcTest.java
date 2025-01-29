package ru.MS_calculator.controllers;

import ru.calculator.controller.CalculatorController;
import ru.library.dto.CreditDto;
import ru.library.dto.EmploymentDto;
import ru.library.dto.ScoringDataDto;
import ru.calculator.services.CalcService;
import ru.calculator.services.OfferService;

import ru.calculator.services.ScoringService;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerCalcTest {
    @Mock
    private ScoringService scoringService;
    @Mock
    private OfferService offerService;
    @Mock
    private CalcService calcService;

    @InjectMocks
    private CalculatorController calculatorController;

    private  Validator validator;
    private static final EmploymentDto employmentDto = EmploymentDto.builder()
            .employmentStatus(EmploymentDto.EmploymentStatus.EMPLOYED)
            .employerINN("0123456789")
            .salary(new BigDecimal("50000"))
            .position(EmploymentDto.Position.MANAGER)
            .workExperienceTotal(18)
            .workExperienceCurrent(3)
            .build();
    private static final ScoringDataDto validRequest = ScoringDataDto.builder()
            .amount(BigDecimal.valueOf(20000))
            .term(6)
            .firstName("John")
            .lastName("Doe")
            .middleName("Smith")
            .gender(ScoringDataDto.Gender.MALE)
            .birthdate(LocalDate.now().minusYears(20))
            .passportSeries("1234")
            .passportNumber("123456")
            .passportIssueDate(LocalDate.now().minusYears(4))
            .passportIssueBranch("УМВД по Пензенской области")
            .maritalStatus(ScoringDataDto.MaritalStatus.MARRIED)
            .dependentAmount(10)
            .employment(employmentDto)
            .accountNumber("12356")
            .isInsuranceEnabled(true)
            .isSalaryClient(false)
            .build();

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testValidScoringDataDto() {
        Set<ConstraintViolation<ScoringDataDto>> violations = validator.validate(validRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    void testInValidScoringDataDto() {
        EmploymentDto employmentDto = EmploymentDto.builder()
                .employmentStatus(EmploymentDto.EmploymentStatus.UNEMPLOYED)
                .employerINN("012345678a")
                .salary(new BigDecimal("833"))
                .position(null)
                .workExperienceTotal(17)
                .workExperienceCurrent(2)
                .build();
        ScoringDataDto dto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(19999))
                .term(5)
                .firstName("John1")
                .lastName("Doe1")
                .middleName("S")
                .gender(null)
                .birthdate(LocalDate.now().minusYears(19))
                .passportSeries("123")
                .passportNumber("1234567")
                .passportIssueDate(LocalDate.now().plusYears(1))
                .passportIssueBranch(null)
                .maritalStatus(null)
                .dependentAmount(null)
                .employment(employmentDto)
                .accountNumber(null)
                .isInsuranceEnabled(null)
                .isSalaryClient(null)
                .build();

        Set<ConstraintViolation<ScoringDataDto>> violations = validator.validate(dto);

        assertEquals(22, violations.size());
    }

    @Test
    void testResponseCreditDto() {
        CreditDto creditDto = calculatorController.calculateCredit(validRequest);
        assertEquals(validRequest.getTerm(), creditDto.getTerm());
    }
}