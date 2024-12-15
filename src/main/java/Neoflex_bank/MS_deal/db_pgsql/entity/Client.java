package Neoflex_bank.MS_deal.db_pgsql.entity;

import Neoflex_bank.MS_calculator.dto.EmploymentDto;
import Neoflex_bank.MS_calculator.dto.ScoringDataDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "client")
//@EntityListeners(AuditingEntityListener.class)
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID client_id;

    @Column(name = "last_name", nullable = false)
    private String last_name;
    @Column(name = "first_name", nullable = false)
    private String first_name;
    @Column(name = "middle_name")
    private String middle_name;

    @Column(name = "birth_date")
    private Date birth_date;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private ScoringDataDto.Gender gender;

    @Column(name = "martial_status")
    @Enumerated(EnumType.STRING)
    private ScoringDataDto.MaritalStatus martial_status;

    @Column(name = "dependent_amount")
    private Integer dependent_amount;

    @Column(name = "passport")
    private String passport;

    @Column(name = "employment")
    @JdbcTypeCode(SqlTypes.JSON)
    private EmploymentDto employment;

    @Column(name = "account_number")
    private String account_number;
}