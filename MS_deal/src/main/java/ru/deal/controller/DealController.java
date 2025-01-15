package ru.deal.controller;

import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;
import ru.deal.dto.FinishRegistrationRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.deal.service.DealService;

import java.util.List;

@RestController
@RequestMapping("/deal")
public class DealController {
    private final DealService dealService;
    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping("/statement")
    @Tag(   name = "Расчёт возможных условий кредита",
            description = """
                    На основе LoanStatementRequestDto создаётся сущность Client и сохраняется в БД.
                    Создаётся Statement со связью на только что созданный Client и сохраняется в БД.
                    Отправляется POST запрос на /calculator/offers МС Калькулятор через RestClient
                    Каждому элементу из списка List<LoanOfferDto> присваивается id созданной заявки (Statement)""")
    public List<LoanOfferDto> initialRegistration(@RequestBody @Validated LoanStatementRequestDto request) {
        return dealService.processClient(request);
    }

    @PostMapping("/offer/select")
    @Tag(   name = "Выбор одного из предложений",
            description = """
                    Достаётся из БД заявка(Statement) по statementId из LoanOfferDto.
                    В заявке обновляется статус, история статусов(List<StatementStatusHistoryDto>),
                    принятое предложение LoanOfferDto устанавливается в поле appliedOffer.
                    Заявка сохраняется.""")
    public void selectOffer(@RequestBody @Validated LoanOfferDto request) {
        dealService.selectOffer(request);
    }

    @PostMapping("/calculate/{statementId}")
    @Tag(   name = "Завершение регистрации + полный подсчёт кредита",
            description = """
                    Достаётся из БД заявка(Statement) по statementId.
                    ScoringDataDto насыщается информацией из FinishRegistrationRequestDto и Client, который хранится в Statement
                    Отправляется POST запрос на /calculator/calc МС Калькулятор с телом ScoringDataDto через RestClient.
                    На основе полученного из кредитного конвейера CreditDto создаётся сущность Credit и сохраняется в базу со статусом CALCULATED.
                    В заявке обновляется статус, история статусов.
                    Заявка сохраняется.""")
    public void completeRegistration(@PathVariable String statementId, @RequestBody @Validated FinishRegistrationRequestDto request) {
        dealService.finishRegistration(statementId, request);
    }

    @PostMapping("/deal/document/{statementId}/send")
    @Tag(name = "Запрос на отправку документов")
    public void sendDocuments(@PathVariable String statementId, @RequestBody @Validated LoanOfferDto request) {
        dealService.sendDocuments(request, statementId);
    }

    @PostMapping("/deal/document/{statementId}/sign")
    @Tag(name = "Запрос на подписание документов")
    public void signRequestDocuments(@PathVariable String statementId, @RequestBody @Validated LoanOfferDto request) {
        dealService.signRequestDocuments(request, statementId);
    }

    @PostMapping("/deal/document/{statementId}/code")
    @Tag(name = "Подписание документов")
    public void signDocuments(@PathVariable String statementId, @RequestBody @Validated LoanOfferDto request) {
        dealService.signDocuments(request, statementId);
    }
}
