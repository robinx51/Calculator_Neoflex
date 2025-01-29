package ru.gateway.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.library.dto.CreditDto;
import ru.gateway.dto.StatementEntityDto;
import ru.library.dto.FinishRegistrationRequestDto;
import ru.library.exception.FeignValidationException;

import java.util.List;

@FeignClient(name = "ms-deal", url = "${deal.url}")
public interface DealFeignClient {
    @PostMapping("/deal/calculate/{statementId}")
    CreditDto finishRegistration(@PathVariable String statementId, @RequestBody FinishRegistrationRequestDto request) throws FeignValidationException;

    @PostMapping("/deal/document/{statementId}/send")
    void createDocuments(@PathVariable String statementId);
    @PostMapping("/deal/document/{statementId}/sign")
    void signRequestDocuments(@PathVariable String statementId);
    @PostMapping("/deal/document/{statementId}/code")
    void signDocuments(@PathVariable String statementId, @RequestParam Integer sesCode) throws FeignValidationException;

    @GetMapping("/deal/admin/statement/{statementId}")
    StatementEntityDto getStatement(@PathVariable String statementId);
    @GetMapping("/deal/admin/statement")
    List<StatementEntityDto> getStatements();
}