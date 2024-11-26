package MS_calculator.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanStatementRequestDto {
    @NotNull
    @Min(value = 20000, message = "Сумма кредита - действительно число, большее или равное 20000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Срок кредита - целое число, большее или равное 6")
    private int term;

    @NotNull(message = "Имя - от 2 до 30 латинских букв")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Имя - от 2 до 30 латинских букв")
    private String firstName;

    @NotNull(message = "Фамилия - от 2 до 30 латинских букв")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Фамилия - от 2 до 30 латинских букв")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Отчество, при наличии - от 2 до 30 латинских букв")
    private String middleName;

    @NotNull @Email
    private String email;

    @NotNull @Adult
    @NotNull(message = "Дата рождения - число в формате гггг-мм-дд, не позднее 18 лет с текущего дня.")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта - 4 цифры")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта - 6 цифр")
    private String passportNumber;


    @Schema(name = "amount", example = "5", required = true, minimum = "20000")
    public BigDecimal getAmount() {
        return amount;
    }

    public LoanStatementRequestDto setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Schema(name = "term", example = "6", required = true)
    public int getTerm() {
        return term;
    }

    public LoanStatementRequestDto setTerm(int term) {
        this.term = term;
        return this;
    }

    @Schema(name = "firstName", example = "Nikita", required = true, pattern = "^[a-zA-Z]{2,30}$")
    public String getFirstName() {
        return firstName;
    }

    public LoanStatementRequestDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Schema(name = "lastName", example = "Kabachek", required = true, pattern = "^[a-zA-Z]{2,30}$")
    public String getLastName() {
        return lastName;
    }

    public LoanStatementRequestDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Schema(name = "middleName", example = "Andreevich", required = false, nullable = true, pattern = "^[a-zA-Z]{2,30}$")
    public String getMiddleName() {
        return middleName;
    }

    public LoanStatementRequestDto setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    @Schema(name = "email", example = "example@mail.ru", required = true, pattern = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
    public String getEmail() {
        return email;
    }

    public LoanStatementRequestDto setEmail(String email) {
        this.email = email;
        return this;
    }

    @Schema(name = "birthdate", example = "2004-05-19", required = true, pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    public LocalDate getBirthdate() {
        return birthdate;
    }

    public LoanStatementRequestDto setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    @Schema(name = "passportSeries", example = "1234", required = true, pattern = "^.{4}")
    public String getPassportSeries() {
        return passportSeries;
    }

    public LoanStatementRequestDto setPassportSeries(String passportSeries) {
        this.passportSeries = passportSeries;
        return this;
    }

    @Schema(name = "passportNumber", example = "123456", required = true, pattern = "^.{6}")
    public String getPassportNumber() {
        return passportNumber;
    }

    public LoanStatementRequestDto setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }
}
