package ru.calculator.dto;

import ru.calculator.annotations.Adult;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder
public class LoanStatementRequestDto {
    @NotNull
    @Min(value = 20000, message = "Сумма кредита - действительно число, большее или равное 20000")
    @Schema(name = "amount", example = "5", minimum = "20000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Срок кредита - целое число, большее или равное 6")
    @Schema(name = "term", example = "6", minimum = "6")
    private int term;

    @NotNull
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

    @NotNull @Email
    @Schema(name = "email", example = "example@mail.ru", pattern = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
    private String email;

    @Adult
    @Schema(name = "birthdate", example = "2004-05-19", pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта - 4 цифры")
    @Schema(name = "passportSeries", example = "1234", pattern = "^.{4}")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта - 6 цифр")
    @Schema(name = "passportNumber", example = "123456", pattern = "^.{6}")
    private String passportNumber;
}
