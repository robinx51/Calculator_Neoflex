package Neoflex_bank.MS_deal.controller;

import Neoflex_bank.MS_calculator.dto.LoanOfferDto;
import Neoflex_bank.MS_calculator.dto.LoanStatementRequestDto;
import Neoflex_bank.MS_deal.dto.FinishRegistrationRequestDto;
import Neoflex_bank.MS_deal.service.StatementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deal")
public class DealController {
    private final StatementService statementService;
    @Autowired
    public DealController(StatementService statementService) {
        this.statementService = statementService;
    }

    @PostMapping("/statement")
    @Tag(   name = "Расчёт возможных условий кредита",
            description = "")
    public List<LoanOfferDto> initialRegistration(@RequestBody @Validated LoanStatementRequestDto request) {
        statementService.processClient(request);
        return statementService.getOffers(request);
    }

    @PostMapping("/offer/select")
    @Tag(   name = "Выбор одного из предложений",
            description = "")
    public void selectOffer(@RequestBody @Validated LoanOfferDto request) {

    }

    @PostMapping("/calculate/{statementId}")
    @Tag(   name = "Завершение регистрации + полный подсчёт кредита",
            description = "")
    public void completeRegistration(@PathVariable String statementId, @RequestBody @Validated FinishRegistrationRequestDto request) {

    }
}
