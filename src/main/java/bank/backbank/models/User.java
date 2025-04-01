package bank.backbank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String name;

    String password;
    double balance;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id")
    Administrator adminincharge;
    @JsonIgnore
    @OneToMany (mappedBy = "user")
    List<income> income;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Loan> loan;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Administrator getAdminincharge() {
        return adminincharge;
    }

    public void setAdminincharge(Administrator adminincharge) {
        this.adminincharge = adminincharge;
    }

    public List<bank.backbank.models.income> getIncome() {
        return income;
    }

    public void setIncome(List<bank.backbank.models.income> income) {
        this.income = income;
    }

    public List<Loan> getLoan() {
        return loan;
    }

    public void setLoan(List<Loan> loan) {
        this.loan = loan;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", adminincharge=" + adminincharge +
                ", income=" + income +
                ", loan=" + loan +
                '}';
    }
}
