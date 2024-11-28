package MS_calculator.Services;

import MS_calculator.DTO.LoanOfferDto;
import MS_calculator.DTO.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final ScoringService scoringService;

    public List<LoanOfferDto> generateOffers(LoanStatementRequestDto request) {
        List<LoanOfferDto> offers = new ArrayList<>(List.of(
                createOffer(true, true, request),
                createOffer(true, false, request),
                createOffer(false, true, request),
                createOffer(false, false, request)
        ));
        Collections.sort(offers);
        return offers;
    }

    private LoanOfferDto createOffer(boolean isInsuranceEnabled,
                                     boolean isSalaryClient,
                                     LoanStatementRequestDto request) {

        BigDecimal totalAmount = scoringService.evaluateTotalAmountByServices(request.getAmount(), isInsuranceEnabled);

        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient);
        return new LoanOfferDto()
                .setStatementId(UUID.randomUUID())
                .setRequestedAmount(request.getAmount())
                .setTotalAmount(totalAmount)
                .setTerm(request.getTerm())
                .setMonthlyPayment(scoringService.getMonthlyPayment(totalAmount, rate, request.getTerm()))
                .setRate(rate)
                .setInsuranceEnabled(isInsuranceEnabled)
                .setSalaryClient(isSalaryClient);
    }
}
