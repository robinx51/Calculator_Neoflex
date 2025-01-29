package ru.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElementDto {
    @NotNull
    @Schema(name = "number", example = "1", minimum = "1")
    private Integer number;

    @NotNull
    @Schema(name = "date", example = "2025-01-01")
    private LocalDate date;

    @NotNull
    @Schema(name = "totalPayment", example = "50.00")
    private BigDecimal totalPayment;    // Общая сумма платежа

    @NotNull
    @Schema(name = "interestPayment", example = "50.00")
    private BigDecimal interestPayment; // Выплата процентов

    @NotNull
    @Schema(name = "debtPayment", example = "50.00")
    private BigDecimal debtPayment;     // Выплата долга

    @NotNull
    @Schema(name = "remainingDebt", example = "50.00")
    private BigDecimal remainingDebt;   // Оставшийся долг
}
