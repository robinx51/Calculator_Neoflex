package ru.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.library.dto.PaymentScheduleElementDto;
import ru.library.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CreditEntityDto {
    @Schema(name = "creditId", example = "UUID")
    private UUID creditId;

    @Schema(name = "amount", example = "100000")
    private BigDecimal amount;

    @Schema(name = "term", example = "24")
    private Integer term;

    @Schema(name = "monthlyPayment", example = "10000")
    private BigDecimal monthlyPayment;

    @Schema(name = "rate", example = "15")
    private BigDecimal rate;

    @Schema(name = "psk", example = "10000")
    private BigDecimal psk;

    @Schema(name = "paymentSchedule", example = "List<PaymentScheduleElementDto>")
    private List<PaymentScheduleElementDto> paymentSchedule;

    @Schema(name = "insuranceEnabled", example = "true")
    private Boolean insuranceEnabled;

    @Schema(name = "salaryClient", example = "true")
    private Boolean salaryClient;

    @Schema(name = "creditStatus", example = "ISSUED")
    private CreditStatus creditStatus;
}
