package Neoflex_bank.MS_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder
public class LoanOfferDto implements Comparable<LoanOfferDto> {
    @NotNull
    @Schema(name = "statementId", example = "5")
    private UUID statementId;

    @NotNull
    @Min(value = 20000, message = "Сумма кредита - действительно число, большее или равное 20000")
    @Schema(name = "requestedAmount", example = "20000", minimum = "20000")
    private BigDecimal requestedAmount;

    @NotNull
    @Schema(name = "totalAmount", example = "20000")
    private BigDecimal totalAmount;

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
    @Schema(name = "isInsuranceEnabled", example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull
    @Schema(name = "isSalaryClient", example = "true")
    private Boolean isSalaryClient;
    @Override
    public int compareTo(LoanOfferDto other) {
        return this.rate.compareTo(other.rate);
    }

}
