package bank.backbank.repositories;

import bank.backbank.models.User;
import bank.backbank.models.income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepositoy extends JpaRepository<income,Long> {
    List<income> findByUserName(String name);
}
