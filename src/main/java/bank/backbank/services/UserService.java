package bank.backbank.services;

import bank.backbank.models.User;
import bank.backbank.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<Map<User,String>> saveUser(User user) {
        userRepository.save(user);
        Map<User, String> response = new HashMap<>();
        response.put(user, "User saved");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity <List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    public ResponseEntity<Map<User,String>> deleteUser(User user) {
        userRepository.delete(user);
        Map<User, String> response = new HashMap<>();
        response.put(user, "User deleted");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<Map<User,String>> updateUser(User user) {
        userRepository.save(user);
        Map<User, String> response = new HashMap<>();
        response.put(user, "User updated");
        return ResponseEntity.ok().body(response);

    }

    public ResponseEntity<Map<User,String>> getUser(long id) {
        Map<User, String> response = new HashMap<>();
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            response.put(user, "User found");
        }
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<User> getUserByName(String name) {
        User useropt = userRepository.findByname(name);
        if (useropt != null) {
            return ResponseEntity.ok().body(useropt);
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> PayTransaction(String payerId, String receiverName, double amount) {
        User payerOptional = userRepository.findByname(payerId);
        if (payerOptional==null) {
            return ResponseEntity.status(404).body(Map.of("message", "Payer not found"));
        }

        User receiverOptional = userRepository.findByname(receiverName);
        if (receiverOptional==null) {
            return ResponseEntity.status(404).body(Map.of("message", "Receiver not found"));
        }

        User payer = payerOptional;
        User receiver = receiverOptional;


        if ((payer.getBalance() - amount) < -10000) {
            return ResponseEntity.status(400).body(Map.of("message", "Payer balance cannot be lower than -10,000"));
        }
        if (amount<0){
            return ResponseEntity.status(400).body(Map.of("message", "Amount cannot be less than 0"));
        }

        payer.setBalance(payer.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);


        userRepository.save(payer);
        userRepository.save(receiver);

        return ResponseEntity.ok(Map.of("message", "Transaction successful"));
    }
}
