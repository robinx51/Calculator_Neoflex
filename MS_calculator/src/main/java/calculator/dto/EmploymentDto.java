package calculator.dto;

import calculator.annotations.Employer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class EmploymentDto {
    public enum EmploymentStatus {
        EMPLOYED, SELF_EMPLOYED, BUSINESS_OWNER, UNEMPLOYED
    }
    public enum Position {
        MANAGER, MIDDLE_MANAGER, TOP_MANAGER
    }

    @NotNull
    @Employer
    @Schema(name = "employmentStatus", example = "EMPLOYED")
    private EmploymentStatus employmentStatus;

    @NotNull
    @Pattern(regexp = "^\\d{10}$")
    @Schema(name = "employerINN", example = "0123456789")
    private String employerINN;

    @NotNull
    @Schema(name = "salary", example = "100000.00")
    private BigDecimal salary;

    @NotNull
    @Schema(name = "position", example = "MANAGER")
    private Position position;

    @NotNull
    @Min(value = 18, message = "Общий стаж менее 18 месяцев")
    @Schema(name = "workExperienceTotal", example = "18", minimum = "18")
    private Integer workExperienceTotal;

    @NotNull
    @Min(value = 3, message = "Текущий стаж менее 3 месяцев")
    @Schema(name = "workExperienceCurrent", example = "3", minimum = "3")
    private Integer workExperienceCurrent;
}
