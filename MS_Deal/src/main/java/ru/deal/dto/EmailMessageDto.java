package ru.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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

    @NotNull @Email
    @Schema(name = "email address", example = "example@mail.ru", pattern = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
    private String address;

    @NotNull
    @Schema(name = "theme", example = "finishRegistration")
    private Theme theme;

    @NotNull
    @Schema(name = "statementId", example = "08018ccd-3424-4e48-a0b6-d831e223636c")
    private UUID statementId;

    @NotNull
    @Schema(name = "text", example = "Text of message")
    private String text;
}
