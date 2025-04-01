package c.practica_final_interfaz;

import Modelos.ApiService;
import Modelos.RetrofitClient;
import Modelos.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Map;

public class MainView {

    @FXML
    private TextField Adminkey;

    @FXML
    private PasswordField Password;

    @FXML
    private TextField User;

    @FXML
    void IniciarSesion(MouseEvent event) {
        String usuario = User.getText();
        String pass = Password.getText();

        Usuario u = new Usuario(usuario, pass,null);

        // Instancia ApiService
        ApiService usuarioService = RetrofitClient.getApiService();

        // Llamada a la API en un hilo separado
        new Thread(() -> {
            try {
                Call<Map<String, Object>> call = usuarioService.iniciarSesion(u);

                Response<Map<String, Object>> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> responseBody = response.body();

                    System.out.println("Respuesta del servidor: " + responseBody);  // 🔍 Verifica la estructura

                    String mensaje = (String) responseBody.get("mensaje");
                    boolean esAdmin = (boolean) responseBody.get("admin");

                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inicio de Sesión");
                        alert.setHeaderText(mensaje);
                        alert.setContentText(esAdmin ? "Bienvenido, Administrador." : "Bienvenido, Usuario.");
                        alert.showAndWait();
                        u.setAdmin(esAdmin);
                        MenuView.recibirUsuario(u);

                        Parent menuParentView = null;


                        try {
                            menuParentView = FXMLLoader.load(getClass().getResource("Menu_view.fxml"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Scene MainMenuScene = new Scene(menuParentView);

                        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        window.setScene(MainMenuScene);
                        window.setResizable(false);
                        window.show();
                    });
                } else {
                    System.out.println("Código de respuesta: " + response.code());
                    System.out.println("Cuerpo de respuesta: " + response.errorBody().string());

                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error en el inicio de sesión");
                        alert.setContentText("Respuesta inesperada del servidor.");
                        alert.showAndWait();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();

                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error de conexión");
                    alert.setContentText("No se pudo conectar con el servidor.");
                    alert.showAndWait();
                });
            }
        }).start();

    }

    @FXML
    void Registro(MouseEvent event) {
        String usuario = User.getText();
        String pass = Password.getText();
        String admin = Adminkey.getText();

        Usuario u = new Usuario(usuario, pass, "root".equals(admin)); // Atajo para el booleano

        ApiService usuarioService = RetrofitClient.getApiService();

        // Llamada
        Call<Usuario> call = usuarioService.register(u);

        // Respuesta
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Platform.runLater(() -> {
                    if (response.isSuccessful()) {
                        showAlert("Registro", "Registro exitoso", Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Error", "No se pudo registrar el usuario.", Alert.AlertType.ERROR);
                    }
                });
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Platform.runLater(() -> showAlert("Error", "Ocurrió un error en la conexión.", Alert.AlertType.ERROR));
            }
        });
    }

    @FXML
    void Salir(MouseEvent event) {
        System.exit(0);
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
