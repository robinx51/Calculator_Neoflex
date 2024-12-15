package Neoflex_bank.MS_deal.db_pgsql.service;

import Neoflex_bank.MS_deal.db_pgsql.entity.Credit;
import Neoflex_bank.MS_deal.db_pgsql.repository.CreditRepository;
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

    public Credit createCredit(boolean insurance_enabled, boolean salary_client) {
        logger.info("Добавление credit в БД");
        Credit credit = new Credit();
        credit.setInsurance_enabled(insurance_enabled);
        credit.setSalary_client(salary_client);
        credit.setCredit_status(Credit.eCreditStatus.PREPARED);
        return creditRepository.save(credit);
    }

    public Credit getCreditById(UUID credit_id){
        return creditRepository.findById(credit_id).orElse(null);
    }

    public void updateCredit(Credit credit) {
        logger.info("Обновление credit с id: {}", credit.getCredit_id());
        if (creditRepository.existsById(credit.getCredit_id())) {
            creditRepository.save(credit);
            logger.info("Credit обновлён успешно");
        } else {
            logger.error("Credit с id: {} не найден", credit.getCredit_id());
        }
    }
}
