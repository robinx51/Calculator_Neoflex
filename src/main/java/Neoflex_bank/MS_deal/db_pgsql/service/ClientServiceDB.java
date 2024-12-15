package Neoflex_bank.MS_deal.db_pgsql.service;

import Neoflex_bank.MS_calculator.dto.LoanStatementRequestDto;
import Neoflex_bank.MS_deal.db_pgsql.entity.Client;
import Neoflex_bank.MS_deal.db_pgsql.entity.Statement;
import Neoflex_bank.MS_deal.db_pgsql.repository.ClientRepository;
import Neoflex_bank.MS_deal.dto.PassportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.UUID;

@Service
public class ClientServiceDB {
    @Autowired
    private ClientRepository clientRepository;
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceDB.class);

    public Client createClient(String last_name, String first_name, String middle_name, Date date, String email, String passportSeries, String passportNumber) {
        /*Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;*/
        logger.info("Добавление client в БД");

        PassportDto passport = PassportDto.builder()
                .passport_id(UUID.randomUUID())
                .passportSeries(passportSeries)
                .passportNumber(passportNumber)
                .build();

        Client client = new Client();
        client.setLast_name(last_name);
        client.setFirst_name(first_name);
        client.setMiddle_name(middle_name);
        client.setBirth_date(date);
        client.setEmail(email);
        client.setPassportDto(passport);

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
        return createClient(request.getLastName(), request.getFirstName(),
                request.getMiddleName(), Date.valueOf(request.getBirthdate()),
                request.getEmail(), request.getPassportSeries(), request.getPassportNumber());
    }

    public void updateClient(Client client) {
        logger.info("Обновление client с id: {}", client.getClient_id());
        if (clientRepository.existsById(client.getClient_id())) {
            clientRepository.save(client);
            logger.info("Client обновлён успешно");
        } else {
            logger.error("Client с id: {} не найден", client.getClient_id());
        }
    }

    public Client getClientById(UUID client_id){
        return clientRepository.findById(client_id).orElse(null);
    }
}
