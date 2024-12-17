package deal.db_pgsql.repository;

import deal.db_pgsql.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CreditRepository  extends JpaRepository<Credit, UUID> {
}
