package MS_calculator.DTO;

import MS_calculator.Annotations.Adult;
import MS_calculator.Annotations.LoanAmountMoreThanSalaries;
import MS_calculator.Annotations.DateBeforeToday;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@LoanAmountMoreThanSalaries
public class ScoringDataDto {
    public enum Gender {
        MALE, FEMALE, NON_BINARY
    }
    public enum MaritalStatus {
        MARRIED, DIVORCED
    }

    @NotNull
    @Min(value = 20000, message = "Сумма кредита - действительно число, большее или равное 20000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Срок кредита - целое число, большее или равное 6")
    private Integer term;

    @NotNull(message = "Имя - от 2 до 30 латинских букв")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Имя - от 2 до 30 латинских букв")
    private String firstName;

    @NotNull(message = "Фамилия - от 2 до 30 латинских букв")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Фамилия - от 2 до 30 латинских букв")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Отчество, при наличии - от 2 до 30 латинских букв")
    private String middleName;

    @NotNull
    private Gender gender;

    @DateBeforeToday @Adult(over = 20)
    @NotNull(message = "Дата рождения - число в формате гггг-мм-дд, не позднее 20 лет с текущего дня.")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта - 4 цифры")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта - 6 цифр")
    private String passportNumber;

    @NotNull
    @DateBeforeToday
    private LocalDate passportIssueDate;

    @NotNull
    private String passportIssueBranch;

    @NotNull
    private MaritalStatus maritalStatus;

    @NotNull
    private Integer dependentAmount;

    @NotNull @Valid
    private EmploymentDto employment;

    @NotNull
    private String accountNumber;

    @NotNull
    private Boolean isInsuranceEnabled;

    @NotNull
    private Boolean isSalaryClient;

    public ScoringDataDto setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ScoringDataDto setTerm(Integer term) {
        this.term = term;
        return this;
    }

    public ScoringDataDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ScoringDataDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ScoringDataDto setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public ScoringDataDto setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public ScoringDataDto setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public ScoringDataDto setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
        return this;
    }

    public ScoringDataDto setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }

    public ScoringDataDto setPassportIssueDate(LocalDate passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
        return this;
    }

    public ScoringDataDto setPassportIssueBranch(String passportIssueBranch) {
        this.passportIssueBranch = passportIssueBranch;
        return this;
    }

    public ScoringDataDto setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public ScoringDataDto setDependentAmount(Integer dependentAmount) {
        this.dependentAmount = dependentAmount;
        return this;
    }

    public ScoringDataDto setEmployment(EmploymentDto employment) {
        this.employment = employment;
        return this;
    }

    public ScoringDataDto setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public ScoringDataDto setInsuranceEnabled(Boolean insuranceEnabled) {
        isInsuranceEnabled = insuranceEnabled;
        return this;
    }

    public ScoringDataDto setSalaryClient(Boolean salaryClient) {
        isSalaryClient = salaryClient;
        return this;
    }
}
