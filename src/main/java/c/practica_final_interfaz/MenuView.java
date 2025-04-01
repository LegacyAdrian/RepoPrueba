package c.practica_final_interfaz;

import Modelos.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuView {

    @FXML
    private Label UserName;

    static Usuario useractual = new Usuario();

    @FXML
    public void initialize() {
    UserName.setText(useractual.getUser());
    }

    public static void recibirUsuario(Usuario u) {
        useractual = u;

    }

    @FXML
    void IrAdministrar(MouseEvent event) throws IOException {
        if (useractual.getAdmin()){
        Parent menuParentView = FXMLLoader.load(getClass().getResource("Administrar_View.fxml"));
        Scene MainMenuScene = new Scene(menuParentView);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(MainMenuScene);
        window.setResizable(false);
        window.show();}
        else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Error");
            alerta.setContentText("No eres Administrador");
            alerta.showAndWait();
        }
    }

    @FXML
    void Salir(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void ircompras(MouseEvent event) throws IOException {
        ComprarView.recibirUsuario(useractual);
        Parent menuParentView = FXMLLoader.load(getClass().getResource("Comprar_View.fxml"));
        Scene MainMenuScene = new Scene(menuParentView);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(MainMenuScene);
        window.setResizable(false);
        window.show();
    }

}
