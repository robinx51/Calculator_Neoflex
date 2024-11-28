package MS_calculator.Services;

import org.springframework.test.context.TestPropertySource;
import MS_calculator.DTO.LoanOfferDto;
import MS_calculator.DTO.LoanStatementRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {
    @Mock
    private ScoringService scoringService;

    @InjectMocks
    private OfferService offerService;

    @BeforeEach
    void setUp() {
        when(scoringService.calculateRate(anyBoolean(), anyBoolean())).thenReturn(new BigDecimal("20"));
    }

    @Test
    void generateOffers() {
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        BigDecimal amount = new BigDecimal("100000");
        request.setAmount(amount);

        BigDecimal totalAmountInsFalse = new BigDecimal("105000");
        BigDecimal totalAmountInsTrue = new BigDecimal("102000");

        List<LoanOfferDto> loanOffers = offerService.generateOffers(request);

        assertEquals(4,loanOffers.size());
        assertEquals(amount, loanOffers.getFirst().getRequestedAmount());
        verify(scoringService, times(4)).evaluateTotalAmountByServices(any(), anyBoolean());
        verify(scoringService, times(4)).calculateRate(anyBoolean(), anyBoolean());
    }
}