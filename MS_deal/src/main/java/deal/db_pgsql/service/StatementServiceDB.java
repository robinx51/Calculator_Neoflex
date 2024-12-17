package deal.db_pgsql.service;

import deal.db_pgsql.entity.Statement;
import deal.db_pgsql.repository.StatementRepository;
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
        statement.setClient_id(client_id);
        return statementRepository.save(statement);
    }

    public void updateStatement(Statement statement) {
        logger.info("Обновление statement с id: {}", statement.getStatement_id());
        if (statementRepository.existsById(statement.getStatement_id())) {
            statementRepository.save(statement);
            logger.info("Statement обновлён успешно");
        } else {
            logger.error("Statement с id: {} не найден", statement.getStatement_id());
        }
    }

    public Statement getStatementById(UUID statement_id){
        return statementRepository.findById(statement_id).orElse(null);
    }
}
