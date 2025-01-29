package ru.library.dto;

import ru.library.annotations.interfaces.DateBeforeToday;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinishRegistrationRequestDto {
    @NotNull
    @Schema(name = "gender", example = "Male", pattern = "^[a-zA-Z]{2,30}$")
    private ScoringDataDto.Gender gender;

    @NotNull
    @Schema(name = "maritalStatus", example = "DIVORCED")
    private ScoringDataDto.MaritalStatus maritalStatus;

    @NotNull
    @Schema(name = "dependentAmount", example = "5")
    private Integer dependentAmount;

    @NotNull @DateBeforeToday
    @Schema(name = "passportIssueDate", example = "2000-01-01")
    private LocalDate passportIssueDate;

    @NotNull
    @Schema(name = "passportIssueBranch", example = "УМВД по Пензенской области")
    private String passportIssueBranch;

    @NotNull
    @Schema(name = "employment", example = "EmploymentDto")
    private EmploymentDto employment;

    @NotNull
    @Schema(name = "accountNumber", example = "0123456John")
    private String accountNumber;
}
