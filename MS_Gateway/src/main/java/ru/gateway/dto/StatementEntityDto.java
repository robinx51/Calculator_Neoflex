package ru.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.library.dto.LoanOfferDto;
import ru.library.dto.StatementStatusHistoryDto;
import ru.library.enums.ApplicationStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StatementEntityDto implements Serializable {

    @Schema(name = "statementId", example = "UUID")
    private UUID statementId;

    @Schema(name = "client", example = "ClientEntityDto")
    private ClientEntityDto client;

    @Schema(name = "credit", example = "CreditEntityDto")
    private CreditEntityDto credit;

    @Schema(name = "status", example = "STATEMENT_CREATED")
    private ApplicationStatus status;

    @Schema(name = "creationDate", example = "2000-01-02 03:04:05.06070")
    private Timestamp creationDate;

    @Schema(name = "appliedOffer", example = "LoanOfferDto")
    private LoanOfferDto appliedOffer;

    @Schema(name = "signDate", example = "2000-01-02 03:04:05.06070")
    private Timestamp signDate;

    @Schema(name = "sesCode", example = "123456")
    private Integer sesCode;

    @Schema(name = "statusHistory", example = "List<StatementStatusHistoryDto>")
    private List<StatementStatusHistoryDto> statusHistory;
}
