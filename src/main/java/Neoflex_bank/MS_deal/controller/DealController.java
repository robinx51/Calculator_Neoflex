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
            description = """
                    На основе LoanStatementRequestDto создаётся сущность Client и сохраняется в БД.
                    Создаётся Statement со связью на только что созданный Client и сохраняется в БД.
                    Отправляется POST запрос на /calculator/offers МС Калькулятор через RestClient
                    Каждому элементу из списка List<LoanOfferDto> присваивается id созданной заявки (Statement)""")
    public List<LoanOfferDto> initialRegistration(@RequestBody @Validated LoanStatementRequestDto request) {
        return statementService.processClient(request);
    }

    @PostMapping("/offer/select")
    @Tag(   name = "Выбор одного из предложений",
            description = """
                    Достаётся из БД заявка(Statement) по statementId из LoanOfferDto.
                    В заявке обновляется статус, история статусов(List<StatementStatusHistoryDto>),
                    принятое предложение LoanOfferDto устанавливается в поле appliedOffer.
                    Заявка сохраняется.""")
    public void selectOffer(@RequestBody @Validated LoanOfferDto request) {

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

    }
}
