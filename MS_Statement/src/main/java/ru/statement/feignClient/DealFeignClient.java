package ru.statement.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "ms-deal", url = "${deal.url}")
public interface DealFeignClient {
    @PostMapping("/deal/statement")
    List<LoanOfferDto> getOffers(@RequestBody LoanStatementRequestDto request);
    @PostMapping("/deal/offer/select")
    void selectOffer(@RequestBody LoanOfferDto request);
}
