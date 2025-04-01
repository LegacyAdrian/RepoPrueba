package bank.backbank.controllers;

import bank.backbank.models.Administrator;
import bank.backbank.models.AuthResponse;
import bank.backbank.repositories.UserRepository;
import bank.backbank.security.JwtTokenUtil;
import bank.backbank.services.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
    private final UserRepository userRepository;


    @Autowired
    public AuthController(UserRepository userRepository, AdministratorService administratorService) {
        this.userRepository = userRepository;

    }
    User userf ;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody bank.backbank.models.User usuario) {
        bank.backbank.models.User foundUser = userRepository.findByname(usuario.getName());
        System.out.println(foundUser.toString());
        if (foundUser == null) {
            return ResponseEntity.status(401).body("Invalid username");
        }
        if (!usuario.getPassword().equals(foundUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        User userDetails = new User(foundUser.getName(), foundUser.getPassword(), Collections.emptyList());
        System.out.println(userDetails);
        userf = userDetails;
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody bank.backbank.models.User usuario) {
        bank.backbank.models.User newUser = new bank.backbank.models.User(usuario.getName(), usuario.getPassword());
        userRepository.save(newUser);
        return ResponseEntity.ok(("Registro registrado con exito"));
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/Hola")
    public ResponseEntity<?> getAll() {
        String token = jwtTokenUtil.generateToken(userf);
        return ResponseEntity.ok().body(userf);
    }
}