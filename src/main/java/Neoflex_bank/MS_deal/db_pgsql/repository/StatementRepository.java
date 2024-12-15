package Neoflex_bank.MS_deal.db_pgsql.repository;

import Neoflex_bank.MS_deal.db_pgsql.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatementRepository extends JpaRepository<Statement, UUID> {
}
