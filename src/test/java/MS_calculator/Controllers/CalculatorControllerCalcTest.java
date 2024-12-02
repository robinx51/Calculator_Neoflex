package MS_calculator.Controllers;

import MS_calculator.CalculatorController;
import MS_calculator.DTO.CreditDto;
import MS_calculator.DTO.EmploymentDto;
import MS_calculator.DTO.ScoringDataDto;
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

    private Validator validator;
    private static ScoringDataDto validRequest;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    public static void setUpRequest(){
        EmploymentDto employmentDto = new EmploymentDto()
                .setEmploymentStatus(EmploymentDto.EmploymentStatus.EMPLOYED)
                .setEmployerINN("0123456789")
                .setSalary(new BigDecimal("50000"))
                .setPosition(EmploymentDto.Position.MANAGER)
                .setWorkExperienceTotal(18)
                .setWorkExperienceCurrent(3);
        validRequest = new ScoringDataDto()
                .setAmount(BigDecimal.valueOf(20000))
                .setTerm(6)
                .setFirstName("John")
                .setLastName("Doe")
                .setMiddleName("Smith")
                .setGender(ScoringDataDto.Gender.MALE)
                .setBirthdate(LocalDate.now().minusYears(20))
                .setPassportSeries("1234")
                .setPassportNumber("123456")
                .setPassportIssueDate(LocalDate.now().minusYears(4))
                .setPassportIssueBranch("УМВД по Пензенской области")
                .setMaritalStatus(ScoringDataDto.MaritalStatus.MARRIED)
                .setDependentAmount(10)
                .setEmployment(employmentDto)
                .setAccountNumber("12356")
                .setInsuranceEnabled(true)
                .setSalaryClient(false);
    }

    @Test
    void testValidScoringDataDto() {
        Set<ConstraintViolation<ScoringDataDto>> violations = validator.validate(validRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    void testInValidScoringDataDto() {
        EmploymentDto employmentDto = new EmploymentDto()
                .setEmploymentStatus(EmploymentDto.EmploymentStatus.UNEMPLOYED)
                .setEmployerINN("012345678a")
                .setSalary(new BigDecimal("833"))
                .setPosition(null)
                .setWorkExperienceTotal(17)
                .setWorkExperienceCurrent(2);
        ScoringDataDto dto = new ScoringDataDto()
                .setAmount(BigDecimal.valueOf(19999))
                .setTerm(5)
                .setFirstName("John1")
                .setLastName("Doe1")
                .setMiddleName("S")
                .setGender(null)
                .setBirthdate(LocalDate.now().minusYears(19))
                .setPassportSeries("123")
                .setPassportNumber("1234567")
                .setPassportIssueDate(LocalDate.now().plusYears(1))
                .setPassportIssueBranch(null)
                .setMaritalStatus(null)
                .setDependentAmount(null)
                .setEmployment(employmentDto)
                .setAccountNumber(null)
                .setInsuranceEnabled(null)
                .setSalaryClient(null);

        Set<ConstraintViolation<ScoringDataDto>> violations = validator.validate(dto);

        for (ConstraintViolation<ScoringDataDto> violation : violations) {
            System.out.println("Поле: " + violation.getPropertyPath() + ", Ошибка: " + violation.getMessage());
        }

        assertEquals(22, violations.size());
    }

    @Test
    void testResponseCreditDto() {
        CreditDto creditDto = calculatorController.Calc(validRequest);
        assertEquals(validRequest.getTerm(), creditDto.getTerm());
    }
}