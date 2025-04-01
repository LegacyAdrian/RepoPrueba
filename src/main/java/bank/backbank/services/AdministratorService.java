package bank.backbank.services;

import bank.backbank.models.Administrator;
import bank.backbank.models.User;
import bank.backbank.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdministratorService {

    AdminRepository adminRepository;
    @Autowired
    public AdministratorService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public  ResponseEntity<Map<Administrator,String>> saveAdministrator(Administrator admin){
        Map<Administrator,String> adminMap = new HashMap<Administrator,String>();
        adminMap.put(admin, "admin");
        adminRepository.save(admin);
        return ResponseEntity.ok(adminMap);
    }

    public ResponseEntity<Map<Administrator,String>> getAdminbyId(long id){
        Map <Administrator,String> response = new HashMap<>();
        Optional<Administrator> admin = adminRepository.findById(id);
        if(admin.isPresent()){
            response.put(admin.get(), "admin");
            return ResponseEntity.ok(response);
        }
        return  ResponseEntity.notFound().build();
    }
    public List<Administrator> getAllAdmin(){
        List<Administrator> adminList = adminRepository.findAll();
        return adminList;
    }
}

