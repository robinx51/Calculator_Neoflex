package ru.deal.dto;

import ru.calculator.annotations.DateBeforeToday;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data @Builder
public class PassportDto {
    @NotNull
    @Schema(name = "passport_id", example = "875fcd78-1ad2-4058-8e61-3709840a74e7")
    private UUID passport_id;

    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта - 4 цифры")
    @Schema(name = "passportSeries", example = "1234", pattern = "^.{4}")
    private String passportSeries;

    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта - 6 цифр")
    @Schema(name = "passportNumber", example = "123456", pattern = "^.{6}")
    private String passportNumber;

    @DateBeforeToday
    @Schema(name = "passportIssueDate", example = "2000-01-01")
    private LocalDate passportIssueDate;

    @Schema(name = "passportIssueBranch", example = "УМВД по Пензенской области")
    private String passportIssueBranch;
}
