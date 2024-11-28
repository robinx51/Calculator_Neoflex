package MS_calculator.DTO;

import MS_calculator.Annotations.Employer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class EmploymentDto {
    public enum EmploymentStatus {
        EMPLOYED, SELF_EMPLOYED, BUSINESS_OWNER, UNEMPLOYED, STUDENT, RETIRED
    }
    public enum Position {
        MANAGER, MIDDLE_MANAGER, TOP_MANAGER, DEVELOPER, ANALYST, ENGINEER
    }

    @NotNull
    @Employer
    private EmploymentStatus employmentStatus;
    @NotNull
    @Pattern(regexp = "^\\d{10}$")
    private String employerINN;
    @NotNull
    private BigDecimal salary;
    @NotNull
    private Position position;
    @NotNull
    @Min(value = 18, message = "Общий стаж менее 18 месяцев")
    private Integer workExperienceTotal;
    @NotNull
    @Min(value = 3, message = "Текущий стаж менее 3 месяцев")
    private Integer workExperienceCurrent;

    public EmploymentDto setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
        return this;
    }

    public EmploymentDto setEmployerINN(String employerINN) {
        this.employerINN = employerINN;
        return this;
    }

    public EmploymentDto setSalary(BigDecimal salary) {
        this.salary = salary;
        return this;
    }

    public EmploymentDto setPosition(Position position) {
        this.position = position;
        return this;
    }

    public EmploymentDto setWorkExperienceTotal(Integer workExperienceTotal) {
        this.workExperienceTotal = workExperienceTotal;
        return this;
    }

    public EmploymentDto setWorkExperienceCurrent(Integer workExperienceCurrent) {
        this.workExperienceCurrent = workExperienceCurrent;
        return this;
    }
}
