package ru.deal.db_pgsql.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.library.dto.EmploymentDto;
import ru.library.dto.ScoringDataDto;
import ru.library.dto.PassportDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "client")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID clientId;

    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private ScoringDataDto.Gender gender;

    @Column(name = "martial_status")
    @Enumerated(EnumType.STRING)
    private ScoringDataDto.MaritalStatus martialStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(name = "passport")
    @JdbcTypeCode(SqlTypes.JSON)
    private PassportDto passportDto;

    @Column(name = "employment")
    @JdbcTypeCode(SqlTypes.JSON)
    private EmploymentDto employment;

    @Column(name = "account_number")
    private String accountNumber;
}