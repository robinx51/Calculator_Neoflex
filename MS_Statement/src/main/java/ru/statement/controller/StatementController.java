package ru.statement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.calculator.dto.*;
import ru.statement.service.StatementService;

import java.util.List;

@RestController
@RequestMapping
public class StatementController {
    private final StatementService statementService;
    @Autowired
    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @PostMapping("/statement")
    @Tag(   name = "Прескоринг + запрос на расчёт возможных условий кредита",
            description =   "1. По API приходит LoanStatementRequestDto \n" +
                            "2. На основе LoanStatementRequestDto происходит прескоринг.\n" +
                            "3. Отправляется POST-запрос на /deal/statement в МС deal через RestClient.\n" +
                            "4. Ответ на API - список из 4х LoanOfferDto от \"худшего\" к \"лучшему\".")
    public List<LoanOfferDto> initialRegistration(@RequestBody @Validated LoanStatementRequestDto request) {
        return statementService.processClient(request);
    }

    @PostMapping("/statement/offer")
    @Tag(   name = "Прескоринг + запрос на расчёт возможных условий кредита",
            description =   "1. По API приходит LoanOfferDto\n" +
                            "2. Отправляется POST-запрос на /deal/offer/select в МС deal через RestClient")
    public void selectOffer(@RequestBody @Validated LoanOfferDto request) {
        statementService.selectOffer(request);
    }
}
