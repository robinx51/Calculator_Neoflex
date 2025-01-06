package ru.deal.db_pgsql.service;

import ru.deal.db_pgsql.entity.Statement;
import ru.deal.db_pgsql.repository.StatementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class StatementServiceDB {
    @Autowired
    private StatementRepository statementRepository;
    private static final Logger logger = LoggerFactory.getLogger(StatementServiceDB.class);

    public Statement createStatement(UUID client_id) {
        logger.info("Добавление statement в БД");
        Statement statement = new Statement();
        statement.setClientId(client_id);
        statement.setStatus(Statement.eApplicationStatus.STATEMENT_CREATED);
        return statementRepository.save(statement);
    }

    public void updateStatement(Statement statement) {
        logger.info("Обновление statement с id: {}", statement.getStatementId());
        if (statementRepository.existsById(statement.getStatementId())) {
            statementRepository.save(statement);
            logger.info("Statement обновлён успешно");
        } else {
            logger.error("Statement с id: {} не найден", statement.getStatementId());
        }
    }

    public Statement getStatementById(UUID statement_id){
        return statementRepository.findById(statement_id).orElse(null);
    }
}
