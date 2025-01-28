package ru.deal.db_pgsql.service;

import jakarta.persistence.EntityNotFoundException;
import ru.deal.db_pgsql.entity.Client;
import ru.deal.db_pgsql.entity.Statement;
import ru.deal.db_pgsql.repository.StatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class StatementServiceDB {
    @Autowired
    public StatementRepository statementRepository;
    private static final Logger logger = LoggerFactory.getLogger(StatementServiceDB.class);

    public Statement createStatement(Client client) {
        logger.info("Добавление statement в БД");
        Statement statement = new Statement();
        statement.setClient(client);
        return statementRepository.save(statement);
    }

    public void updateStatement(Statement statement) {
        logger.info("Обновление statement с id: {}", statement.getStatementId());
        if (statementRepository.existsById(statement.getStatementId())) {
            statementRepository.save(statement);
            logger.info("Statement обновлён успешно");
        } else {
            logger.error("Statement с id: {} не найден", statement.getStatementId());
            throw new EntityNotFoundException("Statement с id: " + statement.getStatementId() + " не найден");
        }
    }

    public Statement getStatementById(UUID statementId) {
        return statementRepository.findById(statementId)
                .orElseThrow(() -> new EntityNotFoundException("Statement с id: " + statementId + " не найден"));
    }

    public List<Statement> getStatements() {
        return statementRepository.findAll();
    }
}
