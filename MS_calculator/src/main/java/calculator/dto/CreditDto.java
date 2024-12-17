package calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class CreditDto {
    @NotNull
    @Min(value = 20000, message = "Сумма кредита - действительно число, большее или равное 20000")
    @Schema(name = "amount", example = "20000", minimum = "20000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Срок кредита - целое число, большее или равное 6")
    @Schema(name = "term", example = "6", minimum = "6")
    private Integer term;

    @NotNull
    @Schema(name = "monthlyPayment", example = "10000.00")
    private BigDecimal monthlyPayment;

    @NotNull
    @Schema(name = "rate", example = "20.00")
    private BigDecimal rate;

    @NotNull
    @Schema(name = "psk", example = "10000.0")
    private BigDecimal psk;

    @NotNull
    @Schema(name = "isInsuranceEnabled", example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(name = "isSalaryClient", example = "true")
    private Boolean isSalaryClient;

    @NotNull
    @Schema(name = "paymentSchedule", example = "List<PaymentScheduleElementDto>")
    private List<PaymentScheduleElementDto> paymentSchedule;
}