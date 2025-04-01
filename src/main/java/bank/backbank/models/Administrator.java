package bank.backbank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;
    String password;

    @JsonIgnore
    @OneToMany(mappedBy = "adminincharge")
    List<User> users;
}
