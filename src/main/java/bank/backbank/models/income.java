package bank.backbank.models;

import jakarta.persistence.*;

import java.sql.Date;
@Entity
public class income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    double amount;
    Date payment_date;
    @ManyToOne (fetch = FetchType.EAGER)
    User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
