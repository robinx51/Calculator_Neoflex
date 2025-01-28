package ru.deal.db_pgsql.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.calculator.dto.LoanOfferDto;
import ru.deal.dto.StatementStatusHistoryDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "statement")
public class Statement implements Serializable {
    public enum eApplicationStatus {
        STATEMENT_CREATED,
        PREAPPROVAL,
        APPROVED,
        CC_DENIED,
        CC_APPROVED,
        PREPARE_DOCUMENTS,
        DOCUMENT_CREATED,
        CLIENT_DENIED,
        DOCUMENT_SIGNED,
        CREDIT_ISSUED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "statement_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID statementId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private eApplicationStatus status;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDto appliedOffer;

    @Column(name = "sign_date")
    private Timestamp signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @Column(name = "status_history")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<StatementStatusHistoryDto> statusHistory;
}
