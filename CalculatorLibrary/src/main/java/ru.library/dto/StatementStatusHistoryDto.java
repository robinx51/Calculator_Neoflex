package ru.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.library.enums.ApplicationStatus;

import java.time.LocalDateTime;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementStatusHistoryDto {
    public enum eChangeType {
        AUTOMATIC, MANUAL
    }

    @Schema(name = "status", example = "STATEMENT_CREATED")
    private ApplicationStatus status;

    @Schema(name = "time", example = "2000-01-01 12:00:00")
    private LocalDateTime time;

    @Schema(name = "changeType", example = "AUTOMATIC")
    private eChangeType changeType;
}
