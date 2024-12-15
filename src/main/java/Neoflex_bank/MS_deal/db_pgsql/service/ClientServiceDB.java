package Neoflex_bank.MS_deal.db_pgsql.service;

import Neoflex_bank.MS_calculator.dto.LoanStatementRequestDto;
import Neoflex_bank.MS_deal.db_pgsql.entity.Client;
import Neoflex_bank.MS_deal.db_pgsql.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class ClientServiceDB {
    @Autowired
    private ClientRepository clientRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceDB.class);

    public Client createClient(String last_name, String first_name, String middle_name, Date date, String email) {
        /*Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;*/
        logger.info("Добавление client в БД");
        Client client = new Client();
        client.setLast_name(last_name);
        client.setFirst_name(first_name);
        client.setMiddle_name(middle_name);
        client.setBirth_date(date);
        client.setEmail(email);
        return clientRepository.save(client);
        /*try {
            logger.debug("Начало транзакции...");
            transaction = session.beginTransaction();
            session.persist(client);
            logger.debug("Данные клиента сохранены, коммит транзакции...");
            transaction.commit();
            logger.debug("Транзакция завершена успешно!");
        } catch (Exception e) {
            if (transaction != null) {
                logger.error("Откат транзакции из-за ошибки: ", e);
                transaction.rollback();
            }
        } finally {
            session.close();
            logger.debug("Сессия закрыта.");
        }
        return client;*/
    }

    public Client createClient(LoanStatementRequestDto request){
        return createClient(request.getLastName(), request.getFirstName(), request.getMiddleName(), Date.valueOf(request.getBirthdate()), request.getEmail());
    }
}
