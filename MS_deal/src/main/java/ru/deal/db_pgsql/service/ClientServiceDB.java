package ru.deal.db_pgsql.service;

import ru.deal.db_pgsql.entity.Client;
import ru.deal.db_pgsql.repository.ClientRepository;
import ru.calculator.dto.LoanStatementRequestDto;
import ru.deal.dto.PassportDto;
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

    public Client createClient(LoanStatementRequestDto request) {
        logger.info("Добавление client в БД");

        Client client = Client.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .birth_date(Date.valueOf(request.getBirthdate()))
                .email(request.getEmail())
                .passportDto(
                        PassportDto.builder()
                                .passport_id(UUID.randomUUID())
                                .passportSeries(request.getPassportSeries())
                                .passportNumber(request.getPassportNumber())
                                .build()
                )
                .build();

        return clientRepository.save(client);
    }

    public void updateClient(Client client) {
        logger.info("Обновление client с id: {}", client.getClientId());
        if (clientRepository.existsById(client.getClientId())) {
            clientRepository.save(client);
            logger.info("Client обновлён успешно");
        } else {
            logger.error("Client с id: {} не найден", client.getClientId());
        }
    }

    public Client getClientById(UUID client_id){
        return clientRepository.findById(client_id).orElse(null);
    }
}
