package deal.db_pgsql.entity;

import calculator.dto.PaymentScheduleElementDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "credit")
public class Credit {
    public enum eCreditStatus {
        CALCULATED, ISSUED, PREPARED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID credit_id;

    @Min(value = 20000, message = "Сумма кредита - действительно число, большее или равное 20000")
    @Schema(name = "amount", example = "20000", minimum = "20000")
    @Column(name = "amount")
    private BigDecimal amount;

    @Min(value = 6, message = "Срок кредита - целое число, большее или равное 6")
    @Schema(name = "term", example = "6", minimum = "6")
    @Column(name = "term")
    private Integer term;

    @Schema(name = "monthly_payment", example = "10000.00")
    @Column(name = "monthly_payment")
    private BigDecimal monthly_payment;

    @Schema(name = "rate", example = "20.00")
    @Column(name = "rate")
    private BigDecimal rate;

    @Schema(name = "psk", example = "10000.0")
    @Column(name = "psk")
    private BigDecimal psk;

    @Schema(name = "payment_schedule", example = "List<PaymentScheduleElementDto>")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_schedule")
    private List<PaymentScheduleElementDto> payment_schedule;

    @Schema(name = "insurance_enabled", example = "true")
    @Column(name = "insurance_enabled")
    private Boolean insurance_enabled;

    @Schema(name = "salary_client", example = "true")
    @Column(name = "salary_client")
    private Boolean salary_client;

    @Schema(name = "credit_status", example = "CALCULATED")
    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private eCreditStatus credit_status;
}
