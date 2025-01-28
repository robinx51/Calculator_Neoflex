package ru.gateway.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.calculator.dto.LoanOfferDto;
import ru.calculator.dto.LoanStatementRequestDto;
import ru.deal.db_pgsql.entity.Statement;
import ru.deal.dto.FinishRegistrationRequestDto;
import ru.deal.exception.FeignValidationException;
import ru.gateway.service.GatewayService;

import java.util.List;

@RestController
@RequestMapping("/gateway")
@RequiredArgsConstructor
public class GatewayController {
    private final GatewayService gatewayService;

    @PostMapping("/statement")
    @Tag(   name = "1. Расчёт возможных условий кредита",
            description = """
                    1. По API приходит LoanStatementRequestDto
                    2. На основе LoanStatementRequestDto происходит прескоринг.
                    3. Отправляется POST-запрос на /statement в МС statement через RestClient.
                    4. Ответ на API - список из 4х LoanOfferDto от "худшего" к "лучшему".""")
    public List<LoanOfferDto> initialRegistration(@RequestBody @Validated LoanStatementRequestDto request) {
        return gatewayService.initialRegistration(request);
    }

    @PostMapping("/statement/offer")
    @Tag(   name = "2. Выбор одного из предложений",
            description =   "1. По API приходит LoanOfferDto\n" +
                    "2. Отправляется POST-запрос на /statement/offer в МС statement через RestClient")
    public void selectOffer(@RequestBody @Validated LoanOfferDto request) {
        gatewayService.selectOffer(request);
    }

    @PostMapping("/calculate/{statementId}")
    @Tag(   name = "3. Завершение регистрации + полный подсчёт кредита",
            description = """
                    Достаётся из БД заявка(Statement) по statementId.
                    ScoringDataDto насыщается информацией из FinishRegistrationRequestDto и Client, который хранится в Statement
                    Отправляется POST запрос на /calculator/calc МС Калькулятор с телом ScoringDataDto через RestClient.
                    На основе полученного из кредитного конвейера CreditDto создаётся сущность Credit и сохраняется в базу со статусом CALCULATED.
                    В заявке обновляется статус, история статусов. Заявка сохраняется.""")
    public void finishRegistration(@PathVariable String statementId, @RequestBody FinishRegistrationRequestDto request) throws FeignValidationException {
        gatewayService.finishRegistration(statementId, request);
    }

    @PostMapping("/document/{statementId}/send")
    @Tag(name = "4. Запрос на отправку документов")
    public void createDocuments(@PathVariable String statementId) {
        gatewayService.sendDocuments(statementId);
    }

    @PostMapping("/document/{statementId}/sign")
    @Tag(name = "5. Запрос на подписание документов")
    public void signRequestDocuments(@PathVariable String statementId) {
        gatewayService.signRequestDocuments(statementId);
    }

    @PostMapping("/document/{statementId}/code")
    @Tag(name = "6. Подписание документов")
    public void signDocuments(@PathVariable String statementId, @RequestParam Integer sesCode) throws ValidationException {
        gatewayService.signDocuments(statementId, sesCode);
    }

    @GetMapping("/admin/statement/{statementId}")
    @Tag(name = "7. Получить заявку по id")
    public Statement getStatement(@PathVariable String statementId) {
        return gatewayService.getStatementById(statementId);
    }

    @GetMapping("/admin/statement")
    @Tag(name = "8. Получить все заявки")
    public List<Statement> getStatements() {
        return gatewayService.getStatements();
    }
}