package ru.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDto {
    public enum Theme {
        finishRegistration,
        createDocuments,
        sendDocuments,
        sendSes,
        creditIssued,
        statementDenied
    }

    @Schema(name = "email address", example = "example@mail.ru", pattern = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
    private String address;

    @Schema(name = "theme", example = "finishRegistration")
    private Theme theme;

    @Schema(name = "statementId", example = "08018ccd-3424-4e48-a0b6-d831e223636c")
    private UUID statementId;

    @Schema(name = "text", example = "Text of message")
    private String text;
}
