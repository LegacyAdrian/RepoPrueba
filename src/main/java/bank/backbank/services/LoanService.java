package bank.backbank.services;

import bank.backbank.models.Loan;
import bank.backbank.models.User;
import bank.backbank.models.income;
import bank.backbank.repositories.LoanRepository;
import bank.backbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LoanService {
    LoanRepository loanRepository;
    UserRepository userRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> saveLoan(Loan loan) {
        Map<Loan,String> loans = new HashMap<>();
        User user = userRepository.findByname(loan.getUser().getName());
        loan.setUser(user);
        List<Loan> activeLoans = loanRepository.findByUserNameAndStatus(loan.getUser().getName(), false);
        if (!activeLoans.isEmpty()) {
            return new ResponseEntity<>("El usuario ya tiene un préstamo activo.", HttpStatus.BAD_REQUEST);
        }
        loanRepository.save(loan);
        loans.put(loan, loan.toString());
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }
    public ResponseEntity<Map<Loan,String>> getAllLoans() {
        Map<Loan,String> loans = new HashMap<>();
        loanRepository.findAll().forEach(loan -> {
            loans.put(loan,loan.toString());
        });
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }
    public ResponseEntity<Map<Loan,String>> getLoanbyId(long id) {
        Map<Loan,String> loans = new HashMap<>();
        Optional<Loan> loan = loanRepository.findById(id);
        if(loan.isPresent()) {
            loans.put(loan.get(),loan.toString());
        }
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }


    public ResponseEntity<?> getLoanFromUserName(String name) {
        List<Loan> loans = loanRepository.findByUserName(name);
        if (loans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(loans);
    }
    public List<Loan> getActiveLoanfromUser(String name) {
        return loanRepository.findByUserNameAndStatus(name,false);
    }

    public ResponseEntity<String> updateLoan(long id, Loan loan) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);

        if (optionalLoan.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No loan found with ID: " + loan.getId());
        }

        Loan existingLoan = optionalLoan.get();
        double previousAmount = existingLoan.getAmount(); // Monto antes del pago
        double payment = previousAmount - loan.getAmount(); // Monto pagado

        if (payment <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payment amount");
        }

        existingLoan.setAmount(loan.getAmount());

        if (loan.getAmount() <= 0) {
            existingLoan.setAmount(0);
            existingLoan.setStatus(true);
        }

        // Buscar usuario y actualizar balance
        User user = userRepository.findByname(loan.getUser().getName());
        user.setBalance(user.getBalance() - payment); // Solo restamos lo que se ha pagado

        userRepository.save(user);
        loanRepository.save(existingLoan);

        return ResponseEntity.ok("Loan updated and balance adjusted successfully");
    }





}
