package bank.backbank.controllers;

import bank.backbank.models.User;
import bank.backbank.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bank/")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("user")
    public ResponseEntity<Map<User,String>> saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
    @GetMapping("users")
    public ResponseEntity<List<User>> getUsers() {
        return userService.getAllUsers();
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("user/{name}")
    public ResponseEntity<User> getUser(@PathVariable String name) {
        return userService.getUserByName(name);
    }
    @PutMapping("user/pay")
    public ResponseEntity<?> payTransaction(
            @RequestParam String payerId,
            @RequestParam String receiverName,
            @RequestParam double amount) {
        return userService.PayTransaction(payerId, receiverName, amount);
    }
}
