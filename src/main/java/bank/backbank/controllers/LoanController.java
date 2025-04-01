package bank.backbank.controllers;

import bank.backbank.models.Loan;
import bank.backbank.models.User;
import bank.backbank.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank/")
@CrossOrigin(origins = "http://localhost:5173")
public class LoanController {
    //Aqui el Service
    LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("loan/{user}")
    public ResponseEntity<?> getLoanByUser(@PathVariable String user) {
        return loanService.getLoanFromUserName(user);

    }
    @PostMapping("loan")
    public ResponseEntity<?> createLoan(@RequestBody Loan loan) {
        return loanService.saveLoan(loan);
    }

    @GetMapping("activeloan/{user}")
    public List<Loan> getActiveLoanByUser(@PathVariable String user) {
        return loanService.getActiveLoanfromUser(user);
    }
    @PutMapping("loan/{id}")
    public ResponseEntity<?> updateLoan(@PathVariable long id, @RequestBody Loan loan) {
        return loanService.updateLoan(id,loan);
    }
}
