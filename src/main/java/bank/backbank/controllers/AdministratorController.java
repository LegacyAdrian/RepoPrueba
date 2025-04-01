package bank.backbank.controllers;

import bank.backbank.models.Administrator;
import bank.backbank.services.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/bank/")
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AdministratorController {

    AdministratorService administratorService;
    @Autowired
    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }
    @PostMapping("admin")
    public ResponseEntity<Map<Administrator,String>> saveAdministrator(@RequestBody Administrator administrator) {
        return administratorService.saveAdministrator(administrator);
    }
    @GetMapping("admin/{id}")
    public ResponseEntity<Map<Administrator,String>> getAdministratorById(@PathVariable long id) {
        return administratorService.getAdminbyId(id);
    }
}
