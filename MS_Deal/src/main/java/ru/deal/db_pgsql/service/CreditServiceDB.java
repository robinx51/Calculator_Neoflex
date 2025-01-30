package ru.deal.db_pgsql.service;

import jakarta.persistence.EntityNotFoundException;
import ru.deal.db_pgsql.entity.Credit;
import ru.deal.db_pgsql.repository.CreditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreditServiceDB {
    @Autowired
    private CreditRepository creditRepository;
    private static final Logger logger = LoggerFactory.getLogger(CreditServiceDB.class);

    public Credit createCredit(boolean insuranceEnabled, boolean salaryClient) {
        logger.info("Добавление credit в БД");
        Credit credit = new Credit();
        credit.setInsuranceEnabled(insuranceEnabled);
        credit.setSalaryClient(salaryClient);
        credit.setCreditStatus(Credit.eCreditStatus.PREPARED);
        return creditRepository.save(credit);
    }

    public Credit getCreditById(UUID creditId){
        return creditRepository.findById(creditId)
                .orElseThrow(() -> new EntityNotFoundException("Credit с id: " + creditId + " не найден"));
    }

    public void updateCredit(Credit credit) {
        logger.info("Обновление credit с id: {}", credit.getCreditId());
        if (creditRepository.existsById(credit.getCreditId())) {
            creditRepository.save(credit);
            logger.info("Credit обновлён успешно");
        } else {
            logger.error("Credit с id: {} не найден", credit.getCreditId());
            throw new EntityNotFoundException("Credit с id: " + credit.getCreditId() + " не найден");
        }
    }
}
