package ru.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.library.dto.EmploymentDto;
import ru.library.dto.PassportDto;
import ru.library.dto.ScoringDataDto;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ClientEntityDto implements Serializable {
    @Schema(name = "clientId", example = "UUID")
    private UUID clientId;

    @Schema(name = "firstName", example = "First")
    private String firstName;

    @Schema(name = "lastName", example = "Last")
    private String lastName;

    @Schema(name = "middleName", example = "Middle")
    private String middleName;

    @Schema(name = "birthDate", example = "2000-01-01")
    private Date birthDate;

    @Schema(name = "email", example = "mail@mail.ru")
    private String email;

    @Schema(name = "gender", example = "MALE")
    private ScoringDataDto.Gender gender;

    @Schema(name = "martialStatus", example = "DIVORCED")
    private ScoringDataDto.MaritalStatus martialStatus;

    @Schema(name = "dependentAmount", example = "10")
    private Integer dependentAmount;

    @Schema(name = "passportDto", example = "PassportDto")
    private PassportDto passportDto;

    @Schema(name = "employment", example = "EmploymentDto")
    private EmploymentDto employment;

    @Schema(name = "accountNumber", example = "anyString")
    private String accountNumber;
}