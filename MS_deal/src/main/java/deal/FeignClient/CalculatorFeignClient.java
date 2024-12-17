package deal.FeignClient;

import calculator.dto.LoanOfferDto;
import calculator.dto.LoanStatementRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "calculator-calculator.neoflex.service", url = "http://localhost:8080")
public interface CalculatorFeignClient {

    @PostMapping("/calculator/offers")
    List<LoanOfferDto> getOffers(@RequestBody LoanStatementRequestDto request);
}
