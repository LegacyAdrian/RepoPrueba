package Modelos;


public class Usuario {
    public Integer id;

    public String user;
    public String password;

    public Usuario() {
    }

    public Usuario(String user, String password, Boolean admin) {
        this.user = user;
        this.password = password;
        this.admin = admin;
    }

    public Usuario(String password, String user) {
        this.password = password;
        this.user = user;
    }

    public Boolean admin = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                '}';
    }
}