package Neoflex_bank.MS_deal.db_pgsql.entity;

import Neoflex_bank.MS_calculator.dto.LoanOfferDto;
import Neoflex_bank.MS_deal.dto.StatementStatusHistoryDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "statement")
//@EntityListeners(AuditingEntityListener.class)
public class Statement implements Serializable {
    public enum eApplicationStatus {
        PREAPPROVAL, APPROVED, CC_DENIED, CC_APPROVED, PREPARE_DOCUMENTS,
        DOCUMENT_CREATED, CLIENT_DENIED, DOCUMENT_SIGNED, CREDIT_ISSUED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "statement_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID statement_id;

    @Column(name = "client_id", columnDefinition = "uuid")
    private UUID client_id;

    @Column(name = "credit_id", columnDefinition = "uuid")
    private UUID credit_id;

    @Column(name = "status")
    private eApplicationStatus status;

    @Column(name = "creation_date")
    private Timestamp creation_date;

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDto applied_offer;

    @Column(name = "sign_date")
    private Timestamp sign_date;

    @Column(name = "ses_code")
    private String ses_code;

    @Column(name = "status_history")
    @JdbcTypeCode(SqlTypes.JSON)
    private StatementStatusHistoryDto status_history;
}
