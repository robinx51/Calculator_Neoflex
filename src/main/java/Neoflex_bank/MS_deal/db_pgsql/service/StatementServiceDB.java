package Neoflex_bank.MS_deal.db_pgsql.service;

import Neoflex_bank.MS_deal.db_pgsql.entity.Statement;
import Neoflex_bank.MS_deal.db_pgsql.repository.StatementRepository;
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
        /*Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;*/
        logger.info("Добавление statement в БД");
        Statement statement = new Statement();
        statement.setClient_id(client_id);
        return statementRepository.save(statement);
        /*try {
            transaction = session.beginTransaction();
            statement.setClient_id(client_id);
            session.persist(statement);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            logger.error(e.getMessage());
        } finally {
            session.close();
        }
        return statement;*/
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
