package MS_calculator.services;

import MS_calculator.dto.LoanOfferDto;
import MS_calculator.dto.LoanStatementRequestDto;
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

    @Test
    void generateOffers() {
        when(scoringService.calculateRate(anyBoolean(), anyBoolean())).thenReturn(new BigDecimal("20"));

        BigDecimal amount = new BigDecimal("100000");
        LoanStatementRequestDto request = LoanStatementRequestDto.builder()
                .amount(amount)
                .build();
        request.setAmount(amount);

        List<LoanOfferDto> loanOffers = offerService.generateOffers(request);

        assertEquals(4,loanOffers.size());
        assertEquals(amount, loanOffers.getFirst().getRequestedAmount());
        verify(scoringService, times(4)).evaluateTotalAmountByServices(any(), anyBoolean());
        verify(scoringService, times(4)).calculateRate(anyBoolean(), anyBoolean());
    }
}