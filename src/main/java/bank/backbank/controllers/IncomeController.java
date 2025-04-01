package bank.backbank.controllers;

import bank.backbank.models.income;
import bank.backbank.models.User;
import bank.backbank.services.IncomeService;
import bank.backbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/bank/")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class IncomeController {
    private final IncomeService incomeService;
    private final UserService userService;

    @Autowired
    public IncomeController(IncomeService incomeService, UserService userService) {
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @PostMapping("income")
    public ResponseEntity<Map<income, String>> saveIncome(@RequestBody income income) {
        return incomeService.saveIncome(income);
    }

    @GetMapping("income/{user}")
    public ResponseEntity<?> getIncomesByUserId(@PathVariable String user) {
        return incomeService.getIncomesFromUserName(user);

    }

    @DeleteMapping("income/{id}")
    public ResponseEntity<Map<String, String>> deleteIncome(@PathVariable long id) {
        return incomeService.deleteIncome(id);
    }

    @PutMapping("income/{id}")
    public ResponseEntity<Map<String, String>> updateIncome(@PathVariable long id, @RequestBody income updatedIncome) {
        return incomeService.updateIncome(id, updatedIncome);
    }
}
