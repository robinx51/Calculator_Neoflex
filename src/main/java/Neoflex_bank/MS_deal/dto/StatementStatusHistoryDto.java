package Neoflex_bank.MS_deal.dto;

import Neoflex_bank.MS_deal.db_pgsql.entity.Statement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Builder
public class StatementStatusHistoryDto {
    public enum eChangeType {
        AUTOMATIC, MANUAL
    }


    @Schema(name = "status", example = "?")
    private Statement.eApplicationStatus status;

    @Schema(name = "time", example = "2000-01-01 12:00:00")
    private LocalDateTime time;

    @Schema(name = "changeType", example = "AUTOMATIC")
    private eChangeType changeType;
}
