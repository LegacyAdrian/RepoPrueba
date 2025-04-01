module c.practica_final_interfaz {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires gson;
    requires java.sql;  // Añadido para acceso a java.sql.Timestamp
    requires okhttp3;
    requires org.apache.commons.net;
    opens c.practica_final_interfaz to javafx.fxml;
    exports c.practica_final_interfaz;
    exports Modelos;
}
