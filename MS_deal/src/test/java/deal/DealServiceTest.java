package deal;

import calculator.dto.CreditDto;
import calculator.dto.LoanOfferDto;
import calculator.dto.LoanStatementRequestDto;
import deal.FeignClient.CalculatorFeignClient;
import deal.db_pgsql.entity.Client;
import deal.db_pgsql.entity.Credit;
import deal.db_pgsql.entity.Statement;
import deal.db_pgsql.service.ClientServiceDB;
import deal.db_pgsql.service.CreditServiceDB;
import deal.db_pgsql.service.StatementServiceDB;
import deal.dto.FinishRegistrationRequestDto;
import deal.dto.PassportDto;
import deal.service.DealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {
    @Mock
    private ClientServiceDB clientServiceDb;
    @Mock
    private StatementServiceDB statementServiceDB;
    @Mock
    private CreditServiceDB creditServiceDB;
    @Mock
    private CalculatorFeignClient calculatorFeignClient;

    @InjectMocks
    private DealService dealService;

    private final Client client = Client.builder().
            client_id(UUID.randomUUID())
            .birth_date(Date.valueOf(LocalDate.now()))
            .passportDto(PassportDto.builder().build())
            .build();
    private Statement statement = Statement.builder()
            .statement_id(UUID.randomUUID())
            .status(Statement.eApplicationStatus.STATEMENT_CREATED)
            .build();
    private final Credit credit = Credit.builder()
            .credit_id(UUID.randomUUID())
            .build();

    @Test
    void processClient() {
        List<LoanOfferDto> feignList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            feignList.add(LoanOfferDto.builder().build());
        }

        when(clientServiceDb.createClient(any())).thenReturn(client);
        when(statementServiceDB.createStatement(any())).thenReturn(statement);
        when(calculatorFeignClient.getOffers(any())).thenReturn(feignList);

        BigDecimal amount = BigDecimal.valueOf(20000);
        LoanStatementRequestDto request = LoanStatementRequestDto.builder()
                .amount(amount)
                .term(6)
                .firstName("John")
                .lastName("Doe")
                .middleName("Smith")
                .email("smith@example.com")
                .birthdate(LocalDate.now().minusYears(18))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        List<LoanOfferDto> response = dealService.processClient(request);

        assertEquals(4, response.size());
        response.forEach(offer -> assertEquals(offer.getStatementId(), statement.getStatement_id()));
    }

    @Test
    void selectOffer() {
        LoanOfferDto request = LoanOfferDto.builder()
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .statementId(statement.getStatement_id())
                .build();
        when(creditServiceDB.createCredit(anyBoolean(), anyBoolean())).thenReturn(credit);
        when(statementServiceDB.getStatementById(request.getStatementId())).thenReturn(statement);

        // Act
        dealService.selectOffer(request);

        // Assert
        assertEquals(Statement.eApplicationStatus.PREPARE_DOCUMENTS, statement.getStatus());
        verify(statementServiceDB).updateStatement(statement);
    }

    @Test
    void finishRegistration() {
        // Arrange
        UUID statementId = statement.getStatement_id();
        FinishRegistrationRequestDto request = FinishRegistrationRequestDto.builder().build();
        BigDecimal amount = BigDecimal.valueOf(100000);
        statement = Statement.builder()
                .client_id(client.getClient_id())
                .credit_id(credit.getCredit_id())
                .applied_offer(LoanOfferDto.builder()
                        .requestedAmount(amount).term(6)
                        .build())
                .build();

        CreditDto creditDto = CreditDto.builder()
                .amount(amount)
                .term(6)
                .build();

        when(statementServiceDB.getStatementById(statementId)).thenReturn(statement);
        when(clientServiceDb.getClientById(statement.getClient_id())).thenReturn(client);
        when(creditServiceDB.getCreditById(statement.getCredit_id())).thenReturn(credit);
        when(calculatorFeignClient.getCreditDto(any())).thenReturn(creditDto);

        // Act
        dealService.finishRegistration(statementId.toString(), request);

        // Assert
        assertEquals(amount, credit.getAmount());
        verify(clientServiceDb).updateClient(client);
        verify(creditServiceDB).updateCredit(credit);
        verify(statementServiceDB).updateStatement(statement);
    }
}