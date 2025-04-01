package bank.backbank.repositories;

import bank.backbank.models.Loan;
import bank.backbank.models.income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserName(String name);
    List<Loan> findByUserNameAndStatus(String name, boolean status);
}
