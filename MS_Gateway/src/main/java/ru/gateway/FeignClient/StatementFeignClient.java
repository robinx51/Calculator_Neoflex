package ru.gateway.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;

import java.util.List;

@FeignClient(name = "ms-statement", url = "${statement.url}")
public interface StatementFeignClient {
    @PostMapping("/statement")
    List<LoanOfferDto> initialRegistration(@RequestBody LoanStatementRequestDto request);
    @PostMapping("/statement/offer")
    void selectOffer(@RequestBody LoanOfferDto request);
}
