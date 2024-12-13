package Neoflex_bank.MS_deal.db_pgsql;

import Neoflex_bank.MS_calculator.dto.LoanStatementRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class DB_neoflex_bank {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DB_neoflex_bank.class);

    public void insertUser(LoanStatementRequestDto request) {
        try (Connection conDB = connectToDB()) {
            if (conDB != null) {
                String query = "INSERT INTO client(last_name, first_name, " +
                        "middle_name, birth_date, email) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement st = conDB.prepareStatement(query);
                st.setString(1, request.getLastName());
                st.setString(2, request.getFirstName());
                st.setString(3, request.getMiddleName());
                st.setObject(4, request.getBirthdate());
                st.setString(5, request.getEmail());

                st.executeUpdate();
                st.close();
                logger.debug("Пользователь " + request.getFirstName() + " " + request.getLastName() + " успешно зарегистрирован.");
            } else {
                logger.error("Пользователь не был добавлен, так как соединение с БД не установлено");
            }
        } catch (SQLException e) {
            logger.error("Ошибка при доступе к базе данных: " + e.getMessage());
        }
    }

    private Connection connectToDB() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/neoflex_bank";

            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "123");

            Connection conDB = DriverManager.getConnection(url, props);

            logger.info("Соединение с базой данных установлено");
            return conDB;
        } catch (ClassNotFoundException | SQLException e) {
            logger.error("Ошибка при доступе к базе данных: " + e.getMessage());
            return null;
        }
    }
}
