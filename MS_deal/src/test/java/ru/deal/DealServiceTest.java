package ru.deal;

import ru.calculator.dto.CreditDto;
import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;
import ru.deal.FeignClient.CalculatorFeignClient;
import ru.deal.db_pgsql.entity.Client;
import ru.deal.db_pgsql.entity.Credit;
import ru.deal.db_pgsql.entity.Statement;
import ru.deal.db_pgsql.service.ClientServiceDB;
import ru.deal.db_pgsql.service.CreditServiceDB;
import ru.deal.db_pgsql.service.StatementServiceDB;
import ru.deal.dto.FinishRegistrationRequestDto;
import ru.deal.dto.PassportDto;
import ru.deal.service.DealService;
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
            clientId(UUID.randomUUID())
            .birth_date(Date.valueOf(LocalDate.now()))
            .passportDto(PassportDto.builder().build())
            .build();
    private Statement statement = Statement.builder()
            .statementId(UUID.randomUUID())
            .status(Statement.eApplicationStatus.STATEMENT_CREATED)
            .build();
    private final Credit credit = Credit.builder()
            .creditId(UUID.randomUUID())
            .build();

    @Test
    void processClient() {
        List<LoanOfferDto> feignList = new ArrayList<>();

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

        response.forEach(offer -> assertEquals(offer.getStatementId(), statement.getStatementId()));
    }

    @Test
    void selectOffer() {
        LoanOfferDto request = LoanOfferDto.builder()
                .isInsuranceEnabled(true)
                .isSalaryClient(true)
                .statementId(statement.getStatementId())
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
        UUID statementId = statement.getStatementId();
        FinishRegistrationRequestDto request = FinishRegistrationRequestDto.builder().build();
        BigDecimal amount = BigDecimal.valueOf(100000);
        statement = Statement.builder()
                .clientId(client.getClientId())
                .creditId(credit.getCreditId())
                .appliedOffer(LoanOfferDto.builder()
                        .requestedAmount(amount).term(6)
                        .build())
                .build();

        CreditDto creditDto = CreditDto.builder()
                .amount(amount)
                .term(6)
                .build();

        when(statementServiceDB.getStatementById(statementId)).thenReturn(statement);
        when(clientServiceDb.getClientById(statement.getClientId())).thenReturn(client);
        when(creditServiceDB.getCreditById(statement.getCreditId())).thenReturn(credit);
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