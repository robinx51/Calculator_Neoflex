package ru.calculator.dto;

import ru.calculator.annotations.Adult;
import ru.calculator.annotations.LoanAmountMoreThanSalaries;
import ru.calculator.annotations.DateBeforeToday;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder
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
    @Schema(name = "amount", example = "5", minimum = "20000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Срок кредита - целое число, большее или равное 6")
    @Schema(name = "term", example = "6", minimum = "6")
    private Integer term;

    @NotNull(message = "Имя - от 2 до 30 латинских букв")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Имя - от 2 до 30 латинских букв")
    @Schema(name = "firstName", example = "John", pattern = "^[a-zA-Z]{2,30}$")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Фамилия - от 2 до 30 латинских букв")
    @Schema(name = "lastName", example = "Doe", pattern = "^[a-zA-Z]{2,30}$")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Отчество, при наличии - от 2 до 30 латинских букв")
    @Schema(name = "middleName", example = "Smith", nullable = true, pattern = "^[a-zA-Z]{2,30}$")
    private String middleName;

    @NotNull
    @Schema(name = "gender", example = "Male", pattern = "^[a-zA-Z]{2,30}$")
    private Gender gender;

    @DateBeforeToday
    @Adult(over = 20)
    @NotNull(message = "Дата рождения - число в формате гггг-мм-дд, не позднее 20 лет с текущего дня.")
    @Schema(name = "birthdate", example = "2000-01-01")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта - 4 цифры")
    @Schema(name = "passportSeries", example = "1234", pattern = "^.{4}")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта - 6 цифр")
    @Schema(name = "passportNumber", example = "123456", pattern = "^.{6}")
    private String passportNumber;

    @NotNull
    @DateBeforeToday
    @Schema(name = "passportIssueDate", example = "2000-01-01")
    private LocalDate passportIssueDate;

    @NotNull
    @Schema(name = "passportIssueBranch", example = "УМВД по Пензенской области")
    private String passportIssueBranch;

    @NotNull
    @Schema(name = "maritalStatus", example = "DIVORCED")
    private MaritalStatus maritalStatus;

    @NotNull
    @Schema(name = "dependentAmount", example = "5")
    private Integer dependentAmount;

    @NotNull @Valid
    @Schema(name = "employment", example = "EmploymentDto")
    private EmploymentDto employment;

    @NotNull
    @Schema(name = "accountNumber", example = "0123456John")
    private String accountNumber;

    @NotNull
    @Schema(name = "isInsuranceEnabled", example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(name = "isSalaryClient", example = "true")
    private Boolean isSalaryClient;
}
