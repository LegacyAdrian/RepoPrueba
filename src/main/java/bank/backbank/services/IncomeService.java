package bank.backbank.services;

import bank.backbank.models.income;
import bank.backbank.models.User;  // Necesitamos importar User
import bank.backbank.repositories.IncomeRepositoy;
import bank.backbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class IncomeService {
    private final UserRepository userRepository;
    IncomeRepositoy repositoy;

    @Autowired
    public IncomeService(IncomeRepositoy repositoy, UserRepository userRepository) {
        this.repositoy = repositoy;
        this.userRepository = userRepository;
    }


    public ResponseEntity<Map<income, String>> saveIncome(income income) {
        Map<income, String> res = new HashMap<>();
        User user = userRepository.findByname(income.getUser().getName());
        income.setUser(user);
        res.put(income, "Data saved");
        repositoy.save(income);
        return ResponseEntity.ok(res);
    }

    public ResponseEntity<?> getIncomesFromUserName(String name) {
        List<income> incomes = repositoy.findByUserName(name);
        if (incomes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(incomes);
    }


    // Eliminar un ingreso por ID
    public ResponseEntity<Map<String, String>> deleteIncome(long id) {
        Optional<income> incomeOptional = repositoy.findById(id);
        if (incomeOptional.isPresent()) {
            repositoy.delete(incomeOptional.get());  // Si existe, se elimina
            Map<String, String> response = new HashMap<>();
            response.put("message", "Income deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Income not found");
            return ResponseEntity.notFound().build();  // Si no existe, retornamos un 404
        }
    }

    // Actualizar un ingreso
    public ResponseEntity<Map<String, String>> updateIncome(long id,income newincome) {
        Optional<income> income2 = repositoy.findById(id);
        if (income2.isPresent()) {
            newincome.setId(id);
            saveIncome(newincome);
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Income updated successfully");
        return ResponseEntity.ok(response);
    }
}
