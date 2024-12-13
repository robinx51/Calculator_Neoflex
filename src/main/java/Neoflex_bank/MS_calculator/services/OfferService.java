package Neoflex_bank.MS_calculator.services;

import Neoflex_bank.MS_calculator.dto.LoanOfferDto;
import Neoflex_bank.MS_calculator.dto.LoanStatementRequestDto;
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
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OfferService.class);

    public List<LoanOfferDto> generateOffers(LoanStatementRequestDto request) {
        logger.info("Обработка запроса на расчёт кредита: сумма = {}, срок = {}, фамилия = {}, имя = {}",
                request.getAmount(),
                request.getTerm(),
                request.getLastName(),
                request.getFirstName());

        List<LoanOfferDto> offers = new ArrayList<>(List.of(
                createOffer(true, true, request),
                createOffer(true, false, request),
                createOffer(false, true, request),
                createOffer(false, false, request)
        ));
        Collections.sort(offers);

        for (LoanOfferDto offer : offers) {
            logger.info("Обработан запрос на расчёт кредита: срок = {}, запрошенная сумма = {}, ставка = {}, общая сумма = {}, ежемесячный платёж = {}",
                    offer.getTerm(),
                    offer.getRequestedAmount(),
                    offer.getRate(),
                    offer.getTotalAmount(),
                    offer.getMonthlyPayment());
        }
        return offers;
    }

    private LoanOfferDto createOffer(boolean isInsuranceEnabled,
                                     boolean isSalaryClient,
                                     LoanStatementRequestDto request) {
        BigDecimal totalAmount = scoringService.evaluateTotalAmountByServices(request.getAmount(), isInsuranceEnabled);

        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient);
        return LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestedAmount(request.getAmount())
                .totalAmount(totalAmount)
                .term(request.getTerm())
                .monthlyPayment(scoringService.getMonthlyPayment(totalAmount, rate, request.getTerm()))
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }
}
